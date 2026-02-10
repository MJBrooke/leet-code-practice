package booking;

import java.util.List;
import java.util.PriorityQueue;

public class MinSum {

    public static int minSum(List<Integer> num, int k) {
        if (num == null || num.isEmpty()) return 0;

        // Build our max-heap using a simple Comparator to ensure the largest is prioritised
        PriorityQueue<Integer> largestNumberQueue = new PriorityQueue<>(
                (a, b) -> b.compareTo(a)
        );
        largestNumberQueue.addAll(num);

        // Get the total sum of the input as-is
        int totalSum = 0;
        for (int n : num)
            totalSum += n;

        for (int i = 0; i < k; i++) {
            // Get the current largest and remove it from our total
            int currentLargest = largestNumberQueue.remove();
            totalSum -= currentLargest;

            // Perform the element math and add the new value back to the total
            int newValue = (int) Math.ceil(currentLargest / 2.0);
            totalSum += newValue;

            // Ensure that we put this value back in the Queue since it might still be the
            // largest number after the element math is done.
            largestNumberQueue.add(newValue);
        }

        return totalSum;
    }
}
