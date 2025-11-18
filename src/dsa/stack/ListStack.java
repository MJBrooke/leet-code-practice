package dsa.stack;

import java.util.ArrayList;
import java.util.List;

/*
Push - O(1)
Pop - O(1)
Peek - O(1)

Can be implemented with a linked list or array list by only appending/removing/peeking at the end.

Useful property - by pushing a sequence of elements into a Stack and then popping them out,
  we effectively reverse the order. (LIFO)
    e.g. 1,2,3 in = 3,2,1 out

  This is useful in some LeetCode problems! Remember LIFO!
 */
public class ListStack {

    private final List<Integer> stack = new ArrayList<>();

    public Integer pop() {
        return stack.removeLast();
        // return list.remove(list.size()-1); // This is the OG way
    }

    public void push(Integer num) {
        stack.add(num);
    }

    public Integer peek() {
        return stack.getLast();
    }
}
