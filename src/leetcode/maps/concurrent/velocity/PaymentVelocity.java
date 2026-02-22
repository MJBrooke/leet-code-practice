package leetcode.maps.concurrent.velocity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PaymentVelocity {
    /*
    Returns the number of times the Card in the payment was seen in the given duration/window.
     */
    public int getUsageCount(Payment payment, Duration duration) {
        return 0;
    }

    public void registerPayment(Payment payment) {

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
        PaymentVelocity provider = new PaymentVelocity();
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
        PaymentVelocity provider = new PaymentVelocity();
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
        PaymentVelocity provider = new PaymentVelocity();
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
