package leetcode.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Given an array of intervals where intervals[i] = [start_i, end_i],
merge all overlapping intervals, and return an array of the
non-overlapping intervals that cover all the intervals in the input.

You may return the answer in any order.

Note: Intervals are non-overlapping if they have no common point.
For example, [1, 2] and [3, 4] are non-overlapping, but [1, 2] and [2, 3] are overlapping.

Example 1:
    Input: intervals = [[1,3],[1,5],[6,7]]
    Output: [[1,5],[6,7]]

Example 2:
    Input: intervals = [[1,2],[2,3]]
    Output: [[1,3]]

Constraints:
    1 <= intervals.length <= 1000
    intervals[i].length == 2
    0 <= start <= end <= 1000
 */
public class MergeIntervals {

    /*
    Understanding the question:
        Okay, so first part of understanding this question is that each {start_i, end_i} represent all numbers inbetween.
        So {1,3} -> 1,2,3 and {2,5} -> 2,3,4,5
        It is not just start/end values.

        And so, we can merge ones with overlapping values:
            {1,3} and {2,5} -> {1,5} (smallest start and largest end)

        So, two intervals overlap if second.start <= first.end
     */

    /*
    Option 1:
        Okay, so we will want two pointers - one to look at prev and next.
        We do logic to see if they overlap.
        If so: Merge, and have prev refer to this new value and next be the next input
        Else, move both pointers

        Some tips after failing some of the test cases:
            - I am assuming that the input is sorted by start time. That is false - that is nowhere in the constraints.
                I should explicitly sort the input. This is the core of the problem: Sort -> Greedy Merge -> Return
            - I should not assume that the end_i value on rhs is higher.

            > Basically, I made some bad assumptions that it would be sorted and always ascending. Read the question better next time!
                This is where I should clarify with the interviewer.

        Complexity:
            Time:
                - sorting full set: n log n
                - iterating over full set: n
                - converting outputL: n

                Overall: n log n
                This is the optimal solution.
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals.length < 2) return intervals;

        // Sort input by start time to ensure we have the correct interval adjacency across the input
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // Create a dynamic list for output, as we do not know how many merges may or may not happen.
        List<int[]> output = new ArrayList<>();

        int[] left = intervals[0];
        for (int i = 1; i < intervals.length; i++) { // We do need to look at all intervals at least once
            int[] right = intervals[i];
            if (start(right) <= end(left)) { // Merge!
                left = new int[]{ // Create new combined entry
                        start(left), // Sorting guarantees the right order here as long as we use left's value
                        Math.max(end(left), end(right))
                };
                // right will be assigned the next entry
            } else { // No overlap
                output.add(left); // This is the biggest possible merge
                left = right; // Since right wasn't merged, we can use it as our new left and allow right to move to the next value.
            }
        }
        output.add(left); // Since we only add to output when there is no overlap, we miss the signal at the end to add the last one.

        return output.toArray(new int[output.size()][]);
    }

    private static int start(int[] arr) {
        return arr[0];
    }

    private static int end(int[] arr) {
        return arr[1];
    }

    /*
    What did I learn?
        - Merging intervals often means:
            1. Sort
            2. Iterate and merge
            3. Return
        - We often _do_ want to store in a new List and convert later, even if it adds extra compute + space.
     */

    static void main() {
        System.out.println(Arrays.deepToString(
                merge(new int[][]{
                        new int[]{1, 3},
                        new int[]{1, 5},
                        new int[]{6, 7}
                })
        )); // Output: [[1,5],[6,7]]

        System.out.println(Arrays.deepToString(
                merge(new int[][]{
                        new int[]{1, 2},
                        new int[]{2, 3}
                })
        )); // Output: [[1,3]]

        System.out.println(Arrays.deepToString(
                merge(new int[][]{
                        new int[]{1, 4},
                        new int[]{0, 4}
                })
        )); // Output: [[0,4]]
    }
}
