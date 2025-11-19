package leetcode.linked.list;

import dsa.linked.list.ListNode;

/*
You are given the heads of two sorted linked lists list1 and list2.

Merge the two lists into one sorted linked list and return the head of the new sorted linked list.

The new list should be made up of nodes from list1 and list2.

Example 1:
    Input: list1 = [1,2,4], list2 = [1,3,5]
    Output: [1,1,2,3,4,5]

Example 2:
    Input: list1 = [], list2 = [1,2]
    Output: [1,2]

Example 3:
    Input: list1 = [], list2 = []
    Output: []

Constraints:
    0 <= The length of each list <= 100.
    -100 <= Node.val <= 100
 */
public class MergeTwoSortedLists {

    /*
    Thoughts:
        We need to keep an iterator for each list, starting at the beginning.
        We need to compare the values to decide which value to use.
        We iterate that pointer forward.
        Do until one list is finished, and then iterate the rest of the other list.
     */
    public static ListNode mergeTwoLists(ListNode lhs, ListNode rhs) {
        if (lhs == null) return rhs;
        if (rhs == null) return lhs;

        ListNode head = null; // Need to store the head to return later, as our iterator will move on
        ListNode curr = null; // Need a reference to an iterator for the new list

        while (lhs != null && rhs != null) {
            ListNode newNode; // New node to be appended
            if (rhs.val >= lhs.val) { // Use the smaller value as the newNodes value
                newNode = new ListNode(lhs.val);
                lhs = lhs.next; // Iterate the list that we removed from
            } else {
                newNode = new ListNode(rhs.val);
                rhs = rhs.next;
            }

            if (head == null) head = newNode; // Store head to return at end of function

            if (curr == null) curr = newNode; // Start iterator at first position
            else {
                // Move the iterator along
                curr.next = newNode;
                curr = curr.next;
            }
        }

        // One list will finish first, in which case we append the rest of the other list in-order

        while (lhs != null) {
            curr.next = new ListNode(lhs.val);
            curr = curr.next;
            lhs = lhs.next;
        }

        while (rhs != null) {
            curr.next = new ListNode(rhs.val);
            curr = curr.next;
            rhs = rhs.next;
        }

        // Return the reference to head that we kept
        return head;
    }

    static void main() {
        var lhs = new ListNode(1, new ListNode(2, new ListNode(4)));
        var rhs = new ListNode(1, new ListNode(3, new ListNode(5)));
        mergeTwoLists(lhs, rhs).print();

        rhs = new ListNode(1, new ListNode(3, new ListNode(5)));
        mergeTwoLists(null, rhs).print();
    }
}
