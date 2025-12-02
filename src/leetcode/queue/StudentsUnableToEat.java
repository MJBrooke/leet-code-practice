package leetcode.queue;

import java.util.*;

/*
The school cafeteria offers circular and square sandwiches at lunch break, referred to by numbers 0 and 1 respectively.
All students stand in a queue. Each student either prefers square or circular sandwiches.

The number of sandwiches in the cafeteria is equal to the number of students.
The sandwiches are placed in a stack. At each step:

If the student at the front of the queue prefers the sandwich on the top of the stack, they will take it and leave the queue.
Otherwise, they will leave it and go to the queue's end.
This continues until none of the queue students want to take the top sandwich and are thus unable to eat.

You are given two integer arrays students and sandwiches where sandwiches[i] is the type of the i^th sandwich in the stack
(i = 0 is the top of the stack) and students[j] is the preference of the j^th student in the initial queue (j = 0 is the front of the queue).
Return the number of students that are unable to eat.

Example 1:

    Input: students = [1,1,0,0], sandwiches = [0,1,0,1]
    Output: 0
    Explanation:
        - Front student leaves the top sandwich and returns to the end of the line making students = [1,0,0,1].
        - Front student leaves the top sandwich and returns to the end of the line making students = [0,0,1,1].
        - Front student takes the top sandwich and leaves the line making students = [0,1,1] and sandwiches = [1,0,1].
        - Front student leaves the top sandwich and returns to the end of the line making students = [1,1,0].
        - Front student takes the top sandwich and leaves the line making students = [1,0] and sandwiches = [0,1].
        - Front student leaves the top sandwich and returns to the end of the line making students = [0,1].
        - Front student takes the top sandwich and leaves the line making students = [1] and sandwiches = [1].
        - Front student takes the top sandwich and leaves the line making students = [] and sandwiches = [].
    Hence, all students are able to eat.

Example 2:

    Input: students = [1,1,1,0,0,1], sandwiches = [1,0,0,0,1,1]
    Output: 3


Constraints:

    1 <= students.length, sandwiches.length <= 100
    students.length == sandwiches.length
    sandwiches[i] is 0 or 1.
    students[i] is 0 or 1.
 */
public class StudentsUnableToEat {

    /*
    Thoughts:
    - We need to keep dequeing and enqueing as we go along.
        At each point, we need to compare the numbers.
        If they match:
            - Remove the student
            - Remove the sandwhich
        If they don't match:
            - Enqueue the student
    - The first example will end up with empty queues on both sides.
        Terminal state: students is empty
      The second example has a sandwhich that does not match any student left in the queue.
        There is a terminal state here - will need to think of how to track it efficiently. Can brute force at first.

    - Implementation: infinite loop with a check?
     */
    public static int countStudents(int[] studentArr, int[] sandwichArr) {
        if (studentArr.length == 0 || sandwichArr.length == 0) return 0;

        Queue<Integer> students = new LinkedList<>(); // Students need FIFO
        Stack<Integer> sandwiches = new Stack<>(); // Sandwiches are LIFO and only taken off the top

        // Add students in-order
        for (int i = 0; i < studentArr.length; i ++) {
            students.add(studentArr[i]);
        }

        // Place sandwiches in reverse order to maintain stack
        for (int i = sandwichArr.length-1; i >= 0; i--) {
            sandwiches.add(sandwichArr[i]);
        }

        // We can track the number of students we have been through without a match - if it equals the size of the students, we are done.
        int numStudentsUnserved = 0;

        while(numStudentsUnserved != students.size()) {
            int currStudent = students.remove();
            if (currStudent == sandwiches.peek()) { // If the current student wants this sandwich
                numStudentsUnserved = 0; // Start with a fresh go-through of the students
                sandwiches.pop(); // Remove the sandwich
                // Do not re-add the student
            } else {
                students.add(currStudent);
                numStudentsUnserved++;
            }
        }

        return numStudentsUnserved;
    }

    static void main() {
        int case1 = countStudents(new int[]{1,1,0,0}, new int[]{0,1,0,1});
        System.out.printf("Num students unserved: %d%n", case1); // Should print '0'

        int case2 = countStudents(new int[]{1,1,1,0,0,1}, new int[]{1,0,0,0,1,1});
        System.out.printf("Num students unserved: %d%n", case2); // Should print '3'
    }
}
