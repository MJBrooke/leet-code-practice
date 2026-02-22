package leetcode.maps.concurrent.velocity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.*;

public class ConcurrentPaymentVelocity {
    /*
    Okay, so we assume that RegisterPayment is going to be invoked multiple times as payments are registered.
    This means that it will end up as a sorted 'list' of payments.
    Since we want to query how many times the associated card has been used, we want to use some kind of map to fetch that quickly.
    We then have the issue of finding the set of payments that fall within the provided duration.

    Option 1: (Naive)
        Given a card ID, we can iterate over the list of payments associated with the Card.
        If their timestamp falls between:
            - Start: payment.timestamp - duration
            - End: payment.timestamp
        We count that payment.
        We could use a ConcurrentHashMap and a synchronizedList (either via synchronize keyword or synchronizedList).
        This would be an O(n) solution since you would iterate every payment.
    Option 2: (Slight improvement)
        We could binary search for the start and end index and minus the former from the latter.
        This would be an O(log n) solution as we would need to search twice for the indexes.
    Option 3: (Another slight improvement)
        We could store the payments in a TreeMap with the timestamp (as a Long) as the Key.
        Since there are no indices in the map, we could then iterate over the submap to count the values.
        This would be an O(log n + k) solution as the submap takes O(log n) and then we iterate over k returned elements.
    Option 4: (Optimal)
        We use a ConcurrentSkipListMap for the data, as this provides a map that is:
            - Concurrency-safe
            - Has ordered-keys
            - Allows for fast ranging over values via submap
        Using this, we can use a subMap operation to get a view of the timestamps
        This is also an O(log n + k) operation, but it is concurrency-safe using built in CAS guarantees from standard library.
     */

    // The outer map provides instant access to all payments for a given card.
    // The inner map provides the number of payments for that card at each Instant.
    private final ConcurrentMap<String, ConcurrentNavigableMap<Instant, Integer>> cardPayments;

    public ConcurrentPaymentVelocity() {
        cardPayments = new ConcurrentHashMap<>();
    }

    /*
    Returns the number of times the Card in the payment was seen in the given duration/window.
     */
    public int getUsageCount(Payment payment, Duration duration) {
        // Fetch only this list of payments related to this card
        var payments = cardPayments.get(payment.hashedCardNumber());
        if (payments == null) return 0;

        var startTime = payment.timestamp().minus(duration);
        var endTime = payment.timestamp();

        /*
         We create a 'view' over the payments given the start/end times based on this payment's timestamp.
         This creates a 'weakly consistent iterator' which is a live window into the underlying map.
         If additional payments are registered into the window during this loop, they may or may not be seen
            depending on how far into the iteration we are.
         This is a core trade-off for concurrency where we are doing a 'best-effort' count under the highly concurrent circumstances.
         In these cases, the severe performance penalty of strict locking outweighs the need for exact counts in a solution like this.
         */
        var paymentsWindow = payments.subMap(
                startTime, true,
                endTime, true
        );

        // We do a simple count of all transactions in this window
        int count = 0;
        for (int transactionsAtInstant : paymentsWindow.values())
            count += transactionsAtInstant;

        return count;
    }

    public void registerPayment(Payment payment) {
        // If this card number has never been seen before, we create a new SkipList for the payment entries
        // This also provides concurrency safety, as only a single thread will create the skip list and all others will receive the same ref.
        var payments = cardPayments.computeIfAbsent(payment.hashedCardNumber(), (_ -> new ConcurrentSkipListMap<>()));

        // For the given timestamp, if:
        //  - There is already a key, we add 1 (using Integer.sum) to the existing value
        //  - There is no key (i.e. a null value for the key), then it simply becomes a 1
        // This is also thread-safe under the hood.
        payments.merge(payment.timestamp(), 1, Integer::sum);
    }

    static void main() throws InterruptedException {
        System.out.println("Initialising tests for ConcurrentVelocityProvider...\n");

        testStandardBehaviour();
        testPaymentsOutsideDuration();
        testConcurrentRegistrations();

        System.out.println("\nAll tests completed.");
    }

    private static String generateId() {
        return UUID.randomUUID().toString();
    }

    private static void testStandardBehaviour() {
        System.out.println("Running testStandardBehaviour...");
        ConcurrentPaymentVelocity provider = new ConcurrentPaymentVelocity();
        String cardHash = "hash123";
        Instant now = Instant.now();

        // Register 3 payments within the last 5 minutes
        provider.registerPayment(new Payment(generateId(), now.minus(Duration.ofMinutes(4)), cardHash));
        provider.registerPayment(new Payment(generateId(), now.minus(Duration.ofMinutes(2)), cardHash));
        provider.registerPayment(new Payment(generateId(), now, cardHash));

        Payment checkPayment = new Payment(generateId(), now, cardHash);
        int count = provider.getUsageCount(checkPayment, Duration.ofMinutes(5));

        System.out.println("Expected count: 3 | Actual count: " + count);
        assert count == 3 : "Standard behaviour test failed";
    }

    private static void testPaymentsOutsideDuration() {
        System.out.println("Running testPaymentsOutsideDuration...");
        ConcurrentPaymentVelocity provider = new ConcurrentPaymentVelocity();
        String cardHash = "hash456";
        Instant now = Instant.now();

        // Register older payments
        provider.registerPayment(new Payment(generateId(), now.minus(Duration.ofMinutes(20)), cardHash));
        provider.registerPayment(new Payment(generateId(), now.minus(Duration.ofMinutes(15)), cardHash));

        // Register recent payments
        provider.registerPayment(new Payment(generateId(), now.minus(Duration.ofMinutes(2)), cardHash));
        provider.registerPayment(new Payment(generateId(), now, cardHash));

        Payment checkPayment = new Payment(generateId(), now, cardHash);
        int count = provider.getUsageCount(checkPayment, Duration.ofMinutes(10));

        System.out.println("Expected count: 2 | Actual count: " + count);
        assert count == 2 : "Payments outside duration test failed";
    }

    private static void testConcurrentRegistrations() throws InterruptedException {
        System.out.println("Running testConcurrentRegistrations...");
        ConcurrentPaymentVelocity provider = new ConcurrentPaymentVelocity();
        String cardHash = "hash789";
        Instant now = Instant.now();

        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                provider.registerPayment(new Payment(generateId(), now, cardHash));
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        Payment checkPayment = new Payment(generateId(), now, cardHash);
        int count = provider.getUsageCount(checkPayment, Duration.ofMinutes(1));

        System.out.println("Expected count: 100 | Actual count: " + count);
        assert count == 100 : "Concurrency test failed";
    }
}
