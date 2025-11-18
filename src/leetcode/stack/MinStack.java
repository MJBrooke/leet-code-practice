package leetcode.stack;

import java.util.ArrayDeque;
import java.util.Deque;

/*
Design a stack class that supports the push, pop, top, and getMin operations.
    - MinStack() initializes the stack object.
    - void push(int val) pushes the element val onto the stack.
    - void pop() removes the element on the top of the stack.
    - int top() gets the top element of the stack.
    - int getMin() retrieves the minimum element in the stack.

Each function should run in O(1) time.

 */
public class MinStack {

    /*
    Thoughts:
        The real tricky part of this question is the `getMin` part.
        With a stack, we only ever have 'access' to the top of the Stack.
        Since we need to do all operations in O(1) time, we cannot traverse some underlying DS.

        So - we need to keep a stack at all times that plainly keeps the things pushed into it.
        Whether a minimum or not, the LIFO must be maintained.

        That means, we need something else to manage these minimums...

     */

    Deque<Integer> fullStack;
    Deque<Integer> minStack;

    public MinStack() {
        fullStack = new ArrayDeque<>();
        minStack = new ArrayDeque<>();
    }

    public void push(int val) {
        fullStack.push(val); // Always push onto our main stack to keep order

        // If we have no minimum, or the next number is even smaller/equal-to than the last known minimum, push it
        if (minStack.isEmpty() || minStack.peek() >= val) minStack.push(val);
    }

    public void pop() {
        // Ignore null-pointer. We always assume non-empty stacks in the question.

        // If what we pop off the min-stack is equal to what is removed from mainStack, pop that minimum too
        if (minStack.peek().equals(fullStack.pop())) minStack.pop();
    }

    public int top() {
        // Ignore null-pointer. We always assume non-empty stacks in the question.
        return fullStack.peek();
    }

    public int getMin() {
        // Ignore null-pointer. We always assume non-empty stacks in the question.
        return minStack.peek();
    }

    static void main() {
        MinStack testStack = new MinStack();
        testStack.push(-100);
        testStack.push(-200);
        testStack.push(-300);
        testStack.push(-400);
        System.out.println(testStack.getMin()); // Should be -400
        testStack.pop();
        testStack.pop();
        System.out.println(testStack.getMin()); // Should be -200


    }
}
