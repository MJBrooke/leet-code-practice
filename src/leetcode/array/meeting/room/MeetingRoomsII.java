package leetcode.array.meeting.room;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/*
Given an array of meeting time interval objects consisting of start and end times [[start_1,end_1],[start_2,end_2],...] (start_i < end_i),
find the minimum number of days required to schedule all meetings without any conflicts
(or alternatively, the number of meeting rooms needed to facilitate them in one day).

Note: (0,8),(8,10) is not considered a conflict at 8.

Example 1:
    Input: intervals = [(0,40),(5,10),(15,20)]
    Output: 2
    Explanation:
        day1: (0,40)
        day2: (5,10),(15,20)

Example 2:
    Input: intervals = [(4,9)]
    Output: 1

Constraints:
    0 <= intervals.length <= 500
    0 <= intervals[i].start < intervals[i].end <= 1,000,000
 */
public class MeetingRoomsII {
    /*
    Understanding the problem:
        Okay, so I did need some hints here to get past the brute force option.

        The theory is that we can use a Min PriorityQueue to store the ending times of each meeting that is in-progress.
        This allows us to see if there is a room available at the start time of any future meeting being added.
        If the room is busy, we know we need to add a day/room to the queue, and if not, an existing room may be reused.
        The maximum size of this priority queue represents the number of days/rooms needed.

        Easy way to explain it:
            I sort meetings by start time.
            I use a min-heap to track ongoing meetings by end time.
            If the earliest ending meeting finishes before the next meeting starts, I reuse that room.
            Otherwise, I allocate a new room.
            The heap size gives the number of rooms needed.
     */

    public static int minMeetingRooms(List<Interval> intervals) {
        if (intervals.isEmpty()) return 0;

        // We need start times to be in order so that we can always compare the next-soonest meeting with the current closest-ending
        intervals.sort((a, b) -> Integer.compare(a.start, b.start));

        // The queue lets us know which meeting is ending soonest
        PriorityQueue<Integer> meetingsInProgress = new PriorityQueue<>();

        for (Interval interval : intervals) {
            // If at least one meeting is in progress and it ends earlier than the new meeting's start
            if (!meetingsInProgress.isEmpty() && meetingsInProgress.peek() <= interval.start)
                meetingsInProgress.remove(); // Remove that meeting

            meetingsInProgress.add(interval.end); // Add the new one in
        }

        // This size represents how many rooms are needed at the maximum overlap of meetings
        return meetingsInProgress.size();
    }

    static void main() {

        /*
        Bucket:
            (1, 5), (5, 9)
            (2, 6)
            (3, 7)
            (4, 8)
         */
        List<Interval> intervals = new java.util.ArrayList<>();
        intervals.add(new Interval(1, 5));
        intervals.add(new Interval(3, 7));
        intervals.add(new Interval(2, 6));
        intervals.add(new Interval(5, 9));
        intervals.add(new Interval(4, 8));
        System.out.println(minMeetingRooms(intervals)); // Output: 4

        intervals = new java.util.ArrayList<>();
        intervals.add(new Interval(0, 40));
        intervals.add(new Interval(5, 10));
        intervals.add(new Interval(15, 20));
        System.out.println(minMeetingRooms(intervals)); // Output: 2

        intervals = new java.util.ArrayList<>();
        intervals.add(new Interval(4, 9));
        System.out.println(minMeetingRooms(intervals)); // Output: 1
    }
}
