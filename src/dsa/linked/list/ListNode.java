package dsa.linked.list;

public class ListNode {
    public int val;
    public ListNode next;
    public ListNode() {}
    public ListNode(int val) { this.val = val; }
    public ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    public void print() {
        System.out.print("[");
        System.out.print(val);

        ListNode next = this.next;
        while(next != null) {
            System.out.print(", " + next.val);
            next = next.next;
        }
        System.out.println("]");
    }
}