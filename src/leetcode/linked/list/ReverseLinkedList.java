package leetcode.linked.list;

import dsa.linked.list.ListNode;

/*
Given the beginning of a singly linked list head,
reverse the list, and return the new beginning of the list.

Example 1:
    Input: head = [0,1,2,3]
    Output: [3,2,1,0]

Example 2:
    Input: head = []
    Output: []

Constraints:
    0 <= The length of the list <= 1000.
    -1000 <= Node.val <= 1000
 */
public class ReverseLinkedList {

    /*
    We want to traverse the list.
    If we keep a pointer to a prev & curr, we can flip the node association.
    In this question, we aren't tracking head/tail, so that isn't a consideration
     */

    public static ListNode reverseList(ListNode head) {
        // If empty, or just one node, no reversal necessary.
        if (head == null || head.next == null) return head;

        ListNode prev = null; // Initial case has prev as null since it will be the new tail
        ListNode curr = head; // Start at initial spot

        while (curr != null) { // We iterate curr, so it will be null when the list is done
            ListNode tmp = curr.next;// Don't lose next, since we are flipping it to prev!
            curr.next = prev; // Flip association

            // Move our 2 pointers forward
            prev = curr;
            curr = tmp;
        }

        // Prev ends up as the original tail while curr is null at this point
        return prev;
    }

    static void main() {
        var head = new ListNode(0, new ListNode(1, new ListNode(2, new ListNode(3))));

        head.print();
        reverseList(head).print();
    }
}
