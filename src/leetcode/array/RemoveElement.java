package leetcode.array;

/*
You are given an integer array nums and an integer val.
Your task is to remove all occurrences of val from nums in-place.

After removing all occurrences of val, return the number of remaining elements, say k,
such that the first k elements of nums do not contain val.

Note:
    - The order of the elements which are not equal to val does not matter.
    - It is not necessary to consider elements beyond the first k positions of the array.
    - To be accepted, the first k elements of nums must contain only elements not equal to val.

Return k as the final result.

Example 1:

    Input: nums = [1,1,2,3,4], val = 1

    Output: [2,3,4]
    Explanation: You should return k = 3 as we have 3 elements which are not equal to val = 1.

Example 2:

    Input: nums = [0,1,2,2,3,0,4,2], val = 2

    Output: [0,1,3,0,4]
    Explanation: You should return k = 5 as we have 5 elements which are not equal to val = 2.

Constraints:

    0 <= nums.length <= 100
    0 <= nums[i] <= 50
    0 <= val <= 100
*/
public class RemoveElement {

    /*
    Some thoughts:
        - Order doesn't matter - seems based on I/O examples that we just maintain the order outside of the val to be removed
        - We know that to the RHS of k, it doesn't matter
        - Duplicates don't matter
    So we can do a similar 2-pointer trick here?
    Ie. Instead of looking at a unique value, we just ensure it isn't the val we don't want.
     */
    private static int removeElement(int[] nums, int unwantedVal) {
        int insertIdx = 0;

        for (int searchIdx = 0; searchIdx < nums.length; searchIdx++) {
            int currVal = nums[searchIdx];
            if (currVal != unwantedVal) { // We want to store this one
                nums[insertIdx++] = currVal;
            }
        }
        return insertIdx;
    }

    static void main() {
        int[] nums = {0,1,2,2,3,0,4,2};
        int numVals = removeElement(nums, 2);
        for (int i = 0; i < numVals; i++) System.out.print(nums[i] + " ");
    }
}
