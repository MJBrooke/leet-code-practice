package leetcode.array;

/*
Given an array of integers nums and an integer target,
return the indices i and j such that nums[i] + nums[j] == target and i != j.

You may assume that every input has exactly one pair of indices i and j that satisfy the condition.
Return the answer with the smaller index first.

Example 1:
    Input: nums = [3,4,5,6], target = 7
    Output: [0,1]
    Explanation: nums[0] + nums[1] == 7, so we return [0, 1].

Example 2:
    Input: nums = [4,5,6], target = 10
    Output: [0,2]

Example 3:
    Input: nums = [5,5], target = 10
    Output: [0,1]

Constraints:
    2 <= nums.length <= 1000
    -10,000,000 <= nums[i] <= 10,000,000
    -10,000,000 <= target <= 10,000,000
    Only one valid answer exists.
 */

import java.util.Arrays;

public class TwoSum {
    public static int[] twoSum(int[] nums, int target) {
        return new int[]{0,0};
    }

    static void main() {
        System.out.println(Arrays.toString(
                twoSum(new int[]{3,4,5,6}, 7)
        )); // Expect [0,1]

        System.out.println(Arrays.toString(
                twoSum(new int[]{4,5,6}, 10)
        )); // Expect [0,2]

        System.out.println(Arrays.toString(
                twoSum(new int[]{5,5}, 10)
        )); // Expect [0,1]
    }

}
