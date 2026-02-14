package leetcode.streams;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionManager {
    // SENIOR TRAP #1: If getSuccessfulVolume is called 1000 times a second,
    // iterating through a 1-million-item list every time is O(N) and will fail the 20x scale test.
    // Consider how you might index or group this data in the constructor.
    private final List<Transaction> transactions;

    public TransactionManager(List<Transaction> transactions) {
        // TODO: Handle the "null list" edge case here defensively
        this.transactions = transactions;
    }

    /**
     * Returns the total sum of all SUCCESSFUL transactions for a merchant.
     */
    public double getSuccessfulVolume(String merchantId) {
        // SENIOR TRAP #2: Using `double` for currency is a massive red flag in fintech
        // due to floating-point precision loss. I've used `double` to match your prompt,
        // but in your HackerRank notes, explicitly state that you would use `BigDecimal`
        // or integer/long cents in a real Adyen production environment.

        // TODO: Implement using Java Streams
        return 0.0;
    }

    /**
     * Returns a list of merchant IDs whose total SUCCESSFUL volume exceeds the threshold.
     */
    public List<String> getHighValueMerchants(double threshold) {
        // TODO: Implement using Java Streams
        return List.of();
    }

    /**
     * Returns a Map where the key is the currency and the value is the total SUCCESSFUL amount.
     */
    public Map<String, Double> groupByCurrency(String merchantId) {
        // TODO: Implement using Java Streams
        return Map.of();
    }

    // --- Domain Models ---

    public enum Status {
        SUCCESS, FAILURE, REFUNDED, PENDING
    }

    public record Transaction(String merchantId, double amount, String currency, Status status) {
    }

    // --- Test Execution ---

    public static void main(String[] args) {
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

        // TEST 4: Null List Edge Case
        try {
            TransactionManager nullManager = new TransactionManager(null);
            runTest("Null List Handling", 0.0, nullManager.getSuccessfulVolume("Merchant_A"));
        } catch (Exception e) {
            System.err.println("❌ FAILED: Null List Handling threw an exception: " + e.getClass().getSimpleName());
        }
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
