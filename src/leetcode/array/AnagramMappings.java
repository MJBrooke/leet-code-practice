package leetcode.array;

import java.util.*;

/*
You are given two integer arrays nums1 and nums2 where nums2 is an anagram of nums1.
Both arrays may contain duplicates.

Return an index mapping array mapping from nums1 to nums2 where
mapping[i] = j means the ith element in nums1 appears in nums2 at index j.
If there are multiple answers, return any of them.

An array a is an anagram of an array b means b is made by randomising the order of the elements in a.

Example 1:
    Input: nums1 = [12,28,46,32,50], nums2 = [50,12,32,46,28]
    Output: [1,4,3,2,0]
    Explanation: As mapping[0] = 1 because the 0th element of nums1 appears at nums2[1], and mapping[1] = 4 because the 1st element of nums1 appears at nums2[4], and so on.

Example 2:
    Input: nums1 = [84,46], nums2 = [84,46]
    Output: [0,1]

Constraints:
    1 <= nums1.length <= 100
    nums2.length == nums1.length
    0 <= nums1[i], nums2[i] <= 10^5
    nums2 is an anagram of nums1
 */
public class AnagramMappings {

    /*
    Option 1:
        We could double-for loop it.
        For each entry in nums1, search the entirety of nums2 for a match.
        Since duplicates exist, we would likely need to track what we have seen to know if we should 'skip' duplicates we have seen.
        Time complexity will be O(n^2) though, so this solution sucks.
    Option 2:
        We could iterate over nums2, creating a HashMap of <NumberValue, List<Index>>.
        We then iterate over nums1, finding the relevant index, creating the return value as we go.

        Complexity:
            n1 = length of nums1
            n2 = length of nums2
            Time:  O(n1 + n2)
            Space: O(n1 + n2) since we create a duplicate set of data from nums2 and the array-value
     */
    public static int[] anagramMappings(int[] nums1, int[] nums2) {
        // No validation strictly needed due to constraints, but added for completeness
        if (nums1.length != nums2.length) return new int[0];

        // Build a Map linking the numbers to their indexes (allowing for duplicates)
        Map<Integer, Deque<Integer>> numberIndexes = new HashMap<>();
        for (int i = 0; i < nums2.length; i++) {
            // An ArrayDeque is very fast and efficient if we are only using `removeFirst`
            var currIndexes = numberIndexes.getOrDefault(nums2[i], new ArrayDeque<>());
            currIndexes.add(i);
            numberIndexes.put(nums2[i], currIndexes);
        }

        //
        int[] mapping = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            int currNum = nums1[i];
            int idx = numberIndexes.get(currNum).removeFirst(); // Safe, since we know it is an anagram
            mapping[i] = idx;
        }

        return mapping;
    }

    static void main() {
        System.out.println(Arrays.toString(
                anagramMappings(new int[]{12,28,46,32,50}, new int[]{50,12,32,46,28})
        )); // Output: [1,4,3,2,0]

        System.out.println(Arrays.toString(
                anagramMappings(new int[]{84,46}, new int[]{84,46})
        )); // Output: [0,1]
    }
}
