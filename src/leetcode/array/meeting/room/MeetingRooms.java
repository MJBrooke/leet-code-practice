package leetcode.array.meeting.room;

import java.util.Arrays;
import java.util.List;

/*
Given an array of meeting time interval objects consisting of start and end times [[start_1,end_1],[start_2,end_2],...] (start_i < end_i),
determine if a person could add all meetings to their schedule without any conflicts.

Note: (0,8),(8,10) is not considered a conflict at 8

Example 1:
    Input: intervals = [(0,30),(5,10),(15,20)]
    Output: false
    Explanation:
        (0,30) and (5,10) will conflict
        (0,30) and (15,20) will conflict

Example 2:
    Input: intervals = [(5,8),(9,15)]
    Output: true

Constraints:
    0 <= intervals.length <= 500
    0 <= intervals[i].start < intervals[i].end <= 1,000,000
 */
public class MeetingRooms {
    /*
    Understanding the problem:
        This is all-or-nothing. Can every meeting given be attended, or not?
        A single conflict is enough to say 'no'.

        For output:
            We only care about a true/false, so no tracking needed on where or which scheduling conflict occurs.
            We can actually just return early on the first conflict we find.

        Okay, constraints say that:
            - start and end within an interval will be in the correct order
            - intervals input can be empty but is bounded to a reasonable number
            - if a start_time == end_time, that is _not_ a conflict
        It does not say:
            - that the intervals will be in any kind of order -> definitely a case to be dealt with
     */

    /*
    Option 1: (Brute Force)
        We can brute-force the solution by doing a double-for loop, comparing each interval with the other.
        If we find any overlap, we exit out.

        What is an overlap?
            If the start time of RHS is smaller than end time of LHS

        However, this has a time complexity of O(n^2) and a space complexity of O(n) since we create an array to optimise access-time.
            We could in theory use List.get to avoid the space complexity but that adds even more time to an already terrible-scaling implementation.
     */
    public static boolean canAttendMeetingsBrute(List<Interval> intervals) {
        if (intervals.size() < 2) return true; // Zero or one meeting can always be attended

        Interval[] list = intervals.toArray(new Interval[intervals.size()]); // O(n) to create, and then O(1) access in iteration

        // Sort by start time
        Arrays.sort(list, (a, b) -> Integer.compare(a.start, b.start));

        for (int i = 0; i < list.length-1; i++)
            for (int j = i+1; j < list.length; j++)
                if (list[j].start < list[i].end) return false;

        return true;
    }

    /*
    Option 2:
        Could we greedily merge and check?
        Ie. If we move through the intervals, taking the maximum end time, could we determine it in a single iteration


     */
    public static boolean canAttendMeetings(List<Interval> intervals) {
        if (intervals.size() < 2) return true; // Zero or one meeting can always be attended

        Interval[] list = intervals.toArray(new Interval[intervals.size()]); // O(n) to create, and then O(1) access in iteration

        // Sort by start time
        Arrays.sort(list, (a, b) -> Integer.compare(a.start, b.start));

        for (int i = 0; i < list.length-1; i++)
            if (list[i].end > list[i+1].start)
                return false;

        return true;
    }


    static void main() {
        System.out.println(canAttendMeetings(List.of(
                new Interval(0, 30),
                new Interval(5, 10),
                new Interval(15, 20)
        ))); // Output: false

        System.out.println(canAttendMeetings(List.of(
                new Interval(5, 8),
                new Interval(9, 15)
        ))); // Output: true

        System.out.println(canAttendMeetings(List.of(
                new Interval(1,2),
                new Interval(2,3),
                new Interval(3,4)
        ))); // Output: true
    }
}
