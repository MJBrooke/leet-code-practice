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

    /*
    Option 1 (Brute force):
        Double-for loop where we multiply all elements except the current element.
        This will be O(n^2) so not great.
     */

    /*
    Option 2:
        Since we are simply multiplying everything together, we can find the total multiplication value of all elements together.
        For each index i, we divide that solution by the value at that index to 'remove' it from that total value.
        We need some careful management of zero values:
            - No zeroes mean we can just use the total/value-at-index
            - A single zero means all other indexes other than that zero have a value of zero
            - Two zeroes mean that everything in the index will be zero

        Complexity:
            - Time: O(n) since we iterate the length of nums twice. So technically O(2n)
     */
    public static int[] productExceptSelf(int[] nums) {
        int[] result = new int[nums.length];

        int total = 1; // Can't be zero, since we are multiplying

        int numZeroes = 0;
        for (int num : nums) {
            if (num == 0) {
                numZeroes++;
                continue;
            }
            total *= num;
        }

        if (numZeroes >= 2) return result;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) // We know 0 does not affect total, so it's just the total as-is
                result[i] = total;
            else if (numZeroes > 0) // If we did find a single zero in the input it means everything else will be zero
                result[i] = 0;
            else // Otherwise, divide away the value at this index
                result[i] = total / nums[i];
        }

        return result;
    }

    static void main() {
        System.out.println(Arrays.toString(
                productExceptSelf(new int[]{0, 0})
        )); // Output: [0,0]

        System.out.println(Arrays.toString(
                productExceptSelf(new int[]{1, 2, 4, 6})
        )); // Output: [48,24,12,8]

        System.out.println(Arrays.toString(
                productExceptSelf(new int[]{-1, 0, 1, 2, 3})
        )); // Output: [0,-6,0,0,0]
    }
}
