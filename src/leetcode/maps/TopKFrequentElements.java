package leetcode.maps;

import java.util.Arrays;

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
    public static int[] topKFrequent(int[] nums, int k) {
        return new int[0];
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
