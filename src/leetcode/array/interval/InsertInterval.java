package leetcode.array.interval;

import java.util.Arrays;

/*
You are given an array of non-overlapping intervals intervals where
intervals[i] = [start_i, end_i] represents the start and the end time of the ith interval.
intervals is initially sorted in ascending order by start_i.
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

    public static int[][] insert(int[][] intervals, int[] newInterval) {
        return null;
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
