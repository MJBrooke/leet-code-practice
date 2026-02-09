package leetcode.maps;

import java.util.*;

/*
Given an integer array nums and an integer k, return the k most frequent elements within the array.
The test cases are generated such that the answer is always unique.
You may return the output in any order.

Example 1:
    Input: nums = [1,2,2,3,3,3], k = 2
    Output: [2,3]

Example 2:
    Input: nums = [7,7], k = 1
    Output: [7]

Constraints:
    1 <= nums.length <= 10^4.
    -1000 <= nums[i] <= 1000
    1 <= k <= number of distinct elements in nums.
 */
public class TopKFrequentElements {

    /*
    Understanding the problem:
        Okay, so we know we need to iterate over each number once, counting them up.
        We are looking for the highest frequency that they appear.
        The trick is that we want to return multiple of the max counts by unique number.
     */

    /*
    Option 1:
        We can iterate over the array keeping a count of each number in a HashMap.
        We then use a Max Heap/PriorityQueue to order map entries by count.
        We then poll k elements from the heap, using the entry key as output in an array.
        This would be an O(n + m + k log m) solution.
     */
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freqs = new HashMap<>();
        for (int num : nums)
            freqs.put(num, freqs.getOrDefault(num, 0)+1);

        PriorityQueue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<>(
                (e1, e2) -> e2.getValue().compareTo(e1.getValue())
        );
        queue.addAll(freqs.entrySet());

        int[] output = new int[k];
        for (int i = 0; i < k; i++)
            output[i] = queue.poll().getKey();

        return output;
    }

    static void main() {
        System.out.println(Arrays.toString(
                topKFrequent(new int[]{1, 2, 2, 3, 3, 3}, 2)
        )); // Output: [2,3]

        System.out.println(Arrays.toString(
                topKFrequent(new int[]{7, 7}, 1)
        )); // Output: [7]
    }
}
