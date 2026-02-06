package leetcode.array.interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
You are given an array of non-overlapping intervals where
intervals[i] = [start_i, end_i] represents the start and the end time of the ith interval.
intervals are initially sorted in ascending order by start_i.
You are given another interval newInterval = [start, end].

Insert newInterval into intervals such that intervals is still sorted in ascending order by start_i
and also intervals still does not have any overlapping intervals.
You may merge the overlapping intervals if needed.
Return intervals after adding newInterval.

Note: Intervals are non-overlapping if they have no common point.
For example, [1,2] and [3,4] are non-overlapping, but [1,2] and [2,3] are overlapping.

Example 1:
    Input: intervals = [[1,3],[4,6]], newInterval = [2,5]
    Output: [[1,6]]

Example 2:
    Input: intervals = [[1,2],[3,5],[9,10]], newInterval = [6,7]
    Output: [[1,2],[3,5],[6,7],[9,10]]

Constraints:
    0 <= intervals.length <= 1000
    newInterval.length == intervals[i].length == 2
    0 <= start <= end <= 1000
 */
public class InsertInterval {

    /*
    Understanding the problem:
        Okay, so this time, if I am reading correctly, we can assume the input is already sorted in ascending order by start time.
        So we do not need to do any initial sort.

        We also already know that all the intervals are already merged (and therefore not overlapping).
        You can almost assume that the input into this is the output from the `merge` operation in MergeIntervals.
     */

    /*
    Option 1:
        Naively, I would think that we could iterate over the intervals and simply insert the new interval at the correct location.
        We could then run a `merge` operation again over the full list (but assuming it is already sorted).
        This would produce a freshly created list of non-overlapping intervals.

        The runtime would be:
            - O(n) to iterate the list and insert the interval into its sorted position
            - O(n) to greedy merge the list
            Total: O(2n) = O(n)

            This of course, assumes that input is already sorted.
     */
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> newIntervals = new ArrayList<>();

        // Traverse the intervals, and insert where start_time is still in order
        boolean inserted = false;
        for (int[] interval : intervals) {
            int currStartTime = interval[0];
            if (!inserted && newInterval[0] <= currStartTime) {
                newIntervals.add(newInterval);
                inserted = true;
            }
            newIntervals.add(interval);
        }

        // If intervals is empty, we catch that case and add just the newInterval
        if (!inserted) newIntervals.add(newInterval);

        // Then merge
        return merge(newIntervals.toArray(new int[newIntervals.size()][]));
    }

    public static int[][] merge(int[][] intervals) {
        List<int[]> mergedIntervals = new ArrayList<>();

        /*
        Okay, we want to do a normal merge here. So:
            For every interval in the list:
                - Check if it should be merged with the adjacent one
                - if not, add it to our list
         */
        int[] left = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            int[] right = intervals[i];

            if (right[0] <= left[1]) {
                left = new int[]{
                        left[0],
                        Math.max(left[1], right[1])
                };
            } else {
                mergedIntervals.add(left);
                left = right;
            }
        }
        mergedIntervals.add(left);

        return mergedIntervals.toArray(new int[mergedIntervals.size()][]);
    }

    static void main() {
        System.out.println(Arrays.deepToString(
                insert(new int[][]{
                        new int[]{1, 3},
                        new int[]{4, 6}
                }, new int[]{2, 5})
        )); // Output: [[1,6]]

        System.out.println(Arrays.deepToString(
                insert(new int[][]{
                        new int[]{1, 2},
                        new int[]{3, 5},
                        new int[]{9, 10}
                }, new int[]{6, 7})
        )); // Output: [[1,2],[3,5],[6,7],[9,10]]
    }
}
