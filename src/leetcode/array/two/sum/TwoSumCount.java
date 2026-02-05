package leetcode.array.two.sum;

import java.util.HashMap;
import java.util.Map;

public class TwoSumCount {

    /*
    When we do a count of all valid matches:
        nums = [1, 5, 7, -1, 5]
        target = 6
        Output = 3
        Pairs: (1,5), (1,5), (7,-1)

    The trick here is that:
        - We must account for the duplicates
        - We don't need indices (so we can rather store something else with what we are looking for)
     */
    public static int twoSum(int[] nums, int target) {
        // <Num,     Count  > Stores how many times we have seen each number in the array
        Map<Integer, Integer> seen = new HashMap<>();
        int count = 0;

        for (int i = 0; i < nums.length; i++) {
            int currentNumber = nums[i];
            int complement = target - currentNumber;

            if (seen.containsKey(complement))
                // If we have a match, we need to count all the times we've previously seen the number
                count += seen.get(complement);

            // We are saying that we have seen this particular number x times
            int currentCountForNumber = seen.getOrDefault(currentNumber, 0) + 1;
            seen.put(currentNumber, currentCountForNumber);
        }

        return count;
    }

    static void main() {
        System.out.println(
                twoSum(new int[]{1, 1, 1, 5}, 6)
        ); // Expect 3
    }

}
