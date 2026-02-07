package leetcode.array.meeting.room;

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
    public static boolean canAttendMeetings(List<Interval> intervals) {
        return false;
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
    }
}
