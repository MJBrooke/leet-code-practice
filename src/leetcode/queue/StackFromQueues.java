package leetcode.queue;

import java.util.LinkedList;
import java.util.Queue;

/*
Implement a last-in-first-out (LIFO) stack using only two queues.
The implemented stack should support all the functions of a normal stack (push, top, pop, and empty).

Implement the MyStack class:
    void push(int x) Pushes element x to the top of the stack.
    int pop()        Removes the element on the top of the stack and returns it.
    int top()        Returns the element on the top of the stack.
    boolean empty()  Returns true if the stack is empty, false otherwise.

Notes:
    You must use only standard operations of a queue, which means that only push to back, peek/pop from front, size and is empty operations are valid.

Example 1:

    Input: ["MyStack", "push", "push", "top", "pop", "empty"]
           [[],        [1],    [2],    [],    [],    []     ]
    Output: [null, null, null, 2, 2, false]
    Explanation:
        MyStack myStack = new MyStack();
        myStack.push(1);
        myStack.push(2);
        myStack.top(); // return 2
        myStack.pop(); // return 2
        myStack.empty(); // return False

Constraints:
    1 <= x <= 9
    At most 100 calls will be made to push, pop, top, and empty.
    All the calls to pop and top are valid.
 */
public class StackFromQueues {

    /*
    Thoughts:
        - We are allowed to use 2 queues. That is the hint - there is something about using 2 FIFO that allows for 1 LIFO...
            As per notes, queues are strictly FIFO - push-to-back and pop-from-front. So no funny business.
        - If we push into a queue, we can't take out of it in the same order.
            So - if we continuously add to a single queue, and only on a pop operation we reverse it until the last element, and put the rest back
            then we have an inefficient Stack. So the second queue is only for temporary storage...
            By filling one FIFO queue and then putting it back as FIFO, we maintain original order.
     */

    Queue<Integer> mainQueue;
    Queue<Integer> popQueue;

    public StackFromQueues() {
        mainQueue = new LinkedList<>();
        popQueue = new LinkedList<>();
    }

    public void push(int x) {
        // We only add to one queue which keeps the right order of elements at all times
        mainQueue.add(x);
    }

    public int pop() {
        int mainQueueSize = mainQueue.size();
        // Remove all but the last element in the main queue (storing them in the popQueue)
        for (int i = 0; i < mainQueueSize-1; i++) {
            popQueue.add(mainQueue.remove());
        }

        // Remove and keep the last element of the main queue which is the LIFO value to pop off
        int lastElement = mainQueue.remove();

        // Re-add all the temp popQueue elements back to the mainQueue
        for (int i = 0; i < mainQueueSize-1; i++) {
            mainQueue.add(popQueue.remove());
        }

        return lastElement;
    }

    // Top is nothing more than a Pop operation that re-adds it back afterwards. Reuse Pop.
    public int top() {
        int lastElement = pop();

        mainQueue.add(lastElement);

        return lastElement;
    }

    public boolean empty() {
        return mainQueue.isEmpty();
    }

    static void main() {
        StackFromQueues myStack = new StackFromQueues();
        myStack.push(10);
        myStack.push(2);
        myStack.push(3);
        System.out.println(myStack.top()); // print 3
        System.out.println(myStack.pop()); // print 3
        System.out.println(myStack.empty()); // print False
    }
}
