package leetcode.array;

import java.util.Arrays;

/*
Given an integer array nums, return an array output
where output[i] is the product of all the elements of nums except nums[i].
Each product is guaranteed to fit in a 32-bit integer.

Follow-up: Could you solve it in
O(n) time without using the division operation?

Example 1:
    Input: nums = [1,2,4,6]
    Output: [48,24,12,8]

Example 2:
    Input: nums = [-1,0,1,2,3]
    Output: [0,-6,0,0,0]

Constraints:
    2 <= nums.length <= 1000
    -20 <= nums[i] <= 20
 */
public class ProductOfArrayExceptSelf {
    public static int[] productExceptSelf(int[] nums) {
        return new int[0];
    }

    static void main() {
        System.out.println(Arrays.toString(
                productExceptSelf(new int[]{1, 2, 4, 6})
        )); // Output: [48,24,12,8]

        System.out.println(Arrays.toString(
                productExceptSelf(new int[]{-1, 0, 1, 2, 3})
        )); // Output: [0,-6,0,0,0]
    }
}
