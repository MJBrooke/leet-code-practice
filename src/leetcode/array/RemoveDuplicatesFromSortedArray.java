package leetcode.array;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class RemoveDuplicatesFromSortedArray {

    // We create a Set of all values in the array.
    //   Take each value from the set, doing an in-place swap for each unique value
    //   We use the size of the set for the return value
    //
    // This is simple, but does require extra O(n) space complexity since at worst case every value in the array is unique
    public static int removeDuplicatesWithSet(int[] nums) {
        Set<Integer> uniqueValues = new LinkedHashSet<>();

        for (int num : nums) {
            uniqueValues.add(num);
        }

        int idx = 0;
        for (int num : uniqueValues) {
            nums[idx++] = num;
        }

        return uniqueValues.size();
    }

    // The idea here is that every time you find a new unique value, you will start inserting in-order
    //   at the 0, 1, 2 index. So if you keep a pointer to where you should next insert
    //   and a pointer to iterate over the loop, you can do O(1) time knowing you can overwrite and
    //   ignore values to the right until a new unique value is found.
    public static int removeDuplicatesTwoPointers(int[] nums) {
        if (nums.length <= 1) return nums.length;

        int lastUniqueValue = nums[0]; // We can assume the first value is unique as it's the only one we have seen
        int insertIdx = 1; // We can go ahead an only start inserting at the second index, since we know values are ascending.

        for (int searchIdx = 1; searchIdx < nums.length; searchIdx++) {
            int currVal = nums[searchIdx];

            // Since values are non-decreasing, we only have to 'remember' the previous unique rather than all uniques
            if (currVal != lastUniqueValue) { // We found a new unique!
                nums[insertIdx++] = currVal; // Insert it at the old position
                lastUniqueValue = currVal; // Keep track of this as the new unique
            }
        }

        return insertIdx;
    }

    static void main() {
        executeAndPrint(new int[]{1,1,2,3,4});
        executeAndPrint(new int[]{2, 10, 10, 30, 30, 30});
    }

    private static void executeAndPrint(int[] nums) {
        System.out.println("Original elements: " + Arrays.toString(nums));

        int numUnique = removeDuplicatesTwoPointers(nums);
        System.out.println("Number of unique elements: " + numUnique);
        for (int i = 0; i < numUnique; i++)
            System.out.print(nums[i] + " ");

        System.out.println("\n---");
    }
}
