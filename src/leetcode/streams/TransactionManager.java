package leetcode.streams;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionManager {
    /*
    Plan for implementation:
        - First pass: Each individual function using streams from the raw transactions object ✅
        - Second pass: Improve performance (caching/indexing at construction, likely)
            Implementation plan: For each function, we can pre-compute the outputs that don't rely on the input value of each function.
             In 2 of the functions, we filter by merchant ID. Precompute to a Map of <Merchant, Txns>.
             We also have two groupings by Merchant's Volume. We can pre-compute that.

            Technically, we could pre-compute everything, but we can make a memory/runtime tradeoff by:
                - Selecting sensible common lookups that are reused in multiple functions
     */

    private final Map<String, List<Transaction>> merchantIndex;
    private final Map<String, Double> merchantVolumeIndex;

    public TransactionManager(List<Transaction> transactions) {
        List<Transaction> source = (transactions == null) ? List.of() : transactions;

        merchantIndex = source.stream()
                .filter(txn -> txn != null && txn.merchantId != null)
                .collect(Collectors.groupingBy(Transaction::merchantId));

        merchantVolumeIndex = source.stream()
                .filter(txn -> txn != null &&
                        txn.merchantId != null &&
                        Objects.equals(Status.SUCCESS, txn.status))
                .collect(Collectors.groupingBy(
                        Transaction::merchantId,
                        Collectors.summingDouble(Transaction::amount)
                ));
    }

    /**
     * Returns the total sum of all SUCCESSFUL transactions for a merchant.
     */
    public double getSuccessfulVolume(String merchantId) {
        // Complexity: O(1) lookup. Completely pre-computed.
        return merchantVolumeIndex.getOrDefault(merchantId, 0.0);

//        Complexity: O(n)
//        return transactions.stream()
//                .filter(txn -> Objects.equals(merchantId, txn.merchantId)) // Choose the filter removing the most entries first! Reduces workload downstream.
//                .filter(txn -> Objects.equals(Status.SUCCESS, txn.status)) // NB: Using Objects.equals avoids NPEs
//                .mapToDouble(Transaction::amount) // mapToDouble prevents Boxing/Unboxing for performance
//                .sum();
    }

    /**
     * Returns a list of merchant IDs whose total SUCCESSFUL volume exceeds the threshold.
     */
    public List<String> getHighValueMerchants(double threshold) {
        // Complexity: O(m) where m = number of unique merchants
        return merchantVolumeIndex.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .toList();

//        Complexity: O(n)
//        return transactions.stream()
//                .filter(txn -> Objects.equals(Status.SUCCESS, txn.status))
//                .collect(Collectors.groupingBy(
//                        Transaction::merchantId,
//                        Collectors.summingDouble(Transaction::amount)
//                )) // Results in Map<String, Double>
//                .entrySet().stream() // We need to create a new stream again
//                .filter(entry -> entry.getValue() > threshold)
//                .map(Map.Entry::getKey)
//                .toList();
    }

    /**
     * Returns a Map where the key is the currency and the value is the total SUCCESSFUL amount.
     */
    public Map<String, Double> groupByCurrency(String merchantId) {
        var merchantTransactions = merchantIndex.get(merchantId);
        if (merchantTransactions == null) return Map.of();

        // Complexity: O(k) where k = number of transactions for this merchant
        return merchantTransactions.stream()
                .filter(txn -> Objects.equals(Status.SUCCESS, txn.status))
                .collect(Collectors.groupingBy(
                        tx -> Objects.requireNonNullElse(tx.currency(), "UNKNOWN"), // In case currency is null
                        Collectors.summingDouble(Transaction::amount)
                ));

//        return transactions.stream()
//                .filter(txn -> Objects.equals(merchantId, txn.merchantId))
//                .filter(txn -> Objects.equals(Status.SUCCESS, txn.status))
//                .collect(Collectors.groupingBy(
//                        Transaction::currency,
//                        Collectors.summingDouble(Transaction::amount)
//                ));
    }

    // --- Domain Models ---

    public enum Status {
        SUCCESS, FAILURE, REFUNDED, PENDING
    }

    public record Transaction(String merchantId, double amount, String currency, Status status) {
    }

    // --- Test Execution ---

    static void main(String[] args) {
        System.out.println("Running BankingTransactionManager Tests...\n");

        List<Transaction> testData = List.of(
                // Merchant A: Mix of currencies and statuses
                new Transaction("Merchant_A", 100.50, "EUR", Status.SUCCESS),
                new Transaction("Merchant_A", 50.00, "EUR", Status.SUCCESS),
                new Transaction("Merchant_A", 200.00, "USD", Status.FAILURE),
                new Transaction("Merchant_A", 10.00, "EUR", Status.REFUNDED),
                new Transaction("Merchant_A", 300.00, "USD", Status.SUCCESS),

                // Merchant B: High volume
                new Transaction("Merchant_B", 5000.00, "GBP", Status.SUCCESS),
                new Transaction("Merchant_B", 1500.00, "GBP", Status.SUCCESS),
                new Transaction("Merchant_B", 500.00, "EUR", Status.PENDING),

                // Merchant C: Only failures/refunds (Volume should be 0)
                new Transaction("Merchant_C", 100.00, "EUR", Status.FAILURE)
        );

        TransactionManager manager = new TransactionManager(testData);

        // TEST 1: Successful Volume
        runTest("Volume for Merchant_A", 450.50, manager.getSuccessfulVolume("Merchant_A"));
        runTest("Volume for Merchant_B", 6500.00, manager.getSuccessfulVolume("Merchant_B"));
        runTest("Volume for Merchant_C (No success)", 0.0, manager.getSuccessfulVolume("Merchant_C"));
        runTest("Volume for Unknown Merchant", 0.0, manager.getSuccessfulVolume("Ghost_Merchant"));
        runTest("null input", 0.0, manager.getSuccessfulVolume(null));

        // TEST 2: High Value Merchants
        List<String> highValueExpected = List.of("Merchant_B");
        runTest("High Value Merchants (> 1000)", highValueExpected, manager.getHighValueMerchants(1000.00));

        List<String> allValueExpected = List.of("Merchant_A", "Merchant_B");
        runTest("High Value Merchants (> 400)", allValueExpected, manager.getHighValueMerchants(400.00));

        // TEST 3: Group By Currency
        Map<String, Double> expectedMapA = Map.of("EUR", 150.50, "USD", 300.00);
        runTest("Group Currency for Merchant_A", expectedMapA, manager.groupByCurrency("Merchant_A"));

        Map<String, Double> expectedMapC = Map.of(); // Empty map expected
        runTest("Group Currency for Merchant_C", expectedMapC, manager.groupByCurrency("Merchant_C"));
    }

    // --- Simple Assertion Helpers ---

    private static void runTest(String testName, Object expected, Object actual) {
        // Handle list comparison regardless of order for the High Value Merchants test
        if (expected instanceof List<?> expectedList && actual instanceof List<?> actualList) {
            boolean pass = expectedList.size() == actualList.size() && expectedList.containsAll(actualList);
            printResult(testName, pass, expected, actual);
            return;
        }

        boolean pass = Objects.equals(expected, actual);
        printResult(testName, pass, expected, actual);
    }

    private static void printResult(String testName, boolean pass, Object expected, Object actual) {
        if (pass) {
            System.out.println("✅ PASS: " + testName);
        } else {
            System.err.println("❌ FAIL: " + testName);
            System.err.println("   Expected: " + expected);
            System.err.println("   Actual:   " + actual);
        }
    }
}
