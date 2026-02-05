package leetcode.array.two.sum;

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
import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    /*
    Brute Force:
    Intuitively, we'd think that we could use a double for-loop to iterate each element against each successive element.
    We would repeat the loop until the outer and inner index point to a pair that matches.
    This would be O(n^2) time complexity and O(1) space complexity.
     */
    public static int[] twoSumBruteForce(int[] nums, int target) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i+1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) return new int[]{i, j};
            }
        }

        return new int[]{0,0}; // Since we know we will always have a valid pair due to constraints, this is just to satisfy the compiler.
    }

    /*
    More intuitive approach:
    Looping more than once creates a lot of repeated work - we are comparing the same numbers over and over.
    We could use a map to store:
        - Key: The complementary number we're looking for, for all numbers we have already seen
        - Val: The index of the original number looking for the key as a partner
    For each iteration of a single loop, we:
        - Check if the current number matches one we've previously looked for and:
            - Return if it is a match, or
            - Add the number/complement to the map if not
     */
    public static int[] twoSum(int[] nums, int target) {
        // <Complement, Index>
        Map<Integer, Integer> complements = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int currentNumber = nums[i];

            // If we have seen a complementary number that matches the current one, we have our match.
            if (complements.containsKey(currentNumber))
                // Get the index of the original number, plus this index for the match
                return new int[]{complements.get(currentNumber), i};

            int complement = target-currentNumber; // This is the number we would need to find to satisfy this iteration's number
            complements.put(complement, i);
        }

        return new int[]{0,0}; // Since we know we will always have a valid pair due to constraints, this is just to satisfy the compiler.
    }

    static void main() {
        System.out.println(Arrays.toString(
                twoSum(new int[]{3,4,5,6}, 7)
        )); // Expect [0,1]

        System.out.println(Arrays.toString(
                twoSum(new int[]{2,5,5,11}, 10)
        )); // Expect [1,2]

        System.out.println(Arrays.toString(
                twoSum(new int[]{5,5}, 10)
        )); // Expect [0,1]
    }

}
