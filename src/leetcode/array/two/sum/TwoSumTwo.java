package leetcode.array.two.sum;

import java.util.Arrays;

/*
Given an array of integers numbers that is sorted in non-decreasing order.
(Ie. Increasing, with possible duplicates in a row)

Return the indices (1-indexed) of two numbers: [index1, index2]
such that they add up to a given target number and index1 < index2.
Note that index1 and index2 cannot be equal, therefore you may not use the same element twice.

There will always be exactly one valid solution.

Your solution must use O(1) additional space.
(This means we cannot use a map!)

Example 1:
    Input: numbers = [1,2,3,4], target = 3
    Output: [1,2]
    Explanation:
        The sum of 1 and 2 is 3.
        Since we are assuming a 1-indexed array, index1 = 1, index2 = 2.
        We return [1, 2].

Constraints:
    2 <= numbers.length <= 1000
    -1000 <= numbers[i] <= 1000
    -1000 <= target <= 1000
 */
public class TwoSumTwo {

    /*
    Brute Force:
        We can always double for-loop, baby!
        Space complexity remains at O(1) but time complexity is unfortunately O(n^2)
     */
    public static int[] twoSumBruteForce(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = i+1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == target) return new int[]{i+1, j+1};
            }
        }

        return new int[]{-1, -1}; // Since we know we will always have a valid pair due to constraints, this is just to satisfy the compiler.
    }

    /*
    Intuitive solution:
        We cannot store a map. So if we want to pass through this array only once, we need something smarter.
        Since we know that the array is sorted, we could always keep two pointers - lhs and rhs starting at each end.
        When combined, you will get one of the following:
            - The target number: We return the indices
            - > target number: We need to make the number smaller, so we move rhs one to the left
            - < target number: We need the target to be bigger, so we move lhs to the right
     */
    public static int[] twoSum(int[] numbers, int target) {
        int lhs = 0;
        int rhs = numbers.length-1;

        while (lhs != rhs) {
            int currentCombination = numbers[lhs] + numbers[rhs];

            if (currentCombination == target) return new int[]{lhs+1, rhs+1};

            if (currentCombination > target) rhs--;
            else lhs++;
        }

        return new int[]{-1, -1}; // Since we know we will always have a valid pair due to constraints, this is just to satisfy the compiler.
    }

    static void main() {
        System.out.println(Arrays.toString(
                twoSum(new int[]{1, 2, 3, 4}, 3)
        ));
    }
}
