package leetcode.maps;

import java.util.*;

/*
Implement the Least Recently Used (LRU) cache class LRUCache.

The class should support the following operations:
    - LRUCache(int capacity)
        Initialize the LRU cache of size capacity.
    - int get(int key)
        Return the value corresponding to the key if the key exists, otherwise return -1.
    - void put(int key, int value)
        Update the value of the key if the key exists.
        Otherwise, add the key-value pair to the cache.
        If the introduction of the new pair causes the cache to exceed its capacity,
        remove the least recently used key.
    - A key is considered used if a get or a put operation is called on it.

Ensure that get and put each run in O(1) average time complexity.

Example 1:
    Input:
        "LRUCache", [2],
        "put", [1, 10],
        "get", [1],
        "put", [2, 20],
        "put", [3, 30],
        "get", [2],
        "get", [1]

    Output:
        [null, null, 10, null, null, 20, -1]

    Explanation:
        LRUCache lRUCache = new LRUCache(2);
        lRUCache.put(1, 10);  // cache: {1=10}
        lRUCache.get(1);      // return 10
        lRUCache.put(2, 20);  // cache: {1=10, 2=20}
        lRUCache.put(3, 30);  // cache: {2=20, 3=30}, key=1 was evicted
        lRUCache.get(2);      // returns 20
        lRUCache.get(1);      // return -1 (not found)

Constraints:
    1 <= capacity <= 100
    0 <= key <= 1000
    0 <= value <= 1000
 */
public class LRUCache {

    /*
    Understanding the question:
        We 100% want to use an underlying Java map to keep the key/values stored.
        The trick here will be adding some way of cleverly tracking which key should be evicted
            if the capacity is reached.
        Additionally, this extra structure must keep get and put at O(1) complexity.
            This means we can't use something like a min-heap.
        We can use a doubly-linked list, where we can insert/delete in O(1) time.
            However, the built-in LinkedList does not offer O(1) search/remove to any Node not at Head/Tail.
            So we will have to build a custom Node-based DLL.
     */

    private static class Node {
        int key;
        int value;
        Node prev;
        Node next;

        public Node() {
            this(0, 0);
        }

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<Integer, Node> map;

    /*
    Important implementation feature (invariant):
        These are Sentinel nodes that will always be present and never deleted.
        They are always the head and tail, and are there to simplify our insert/remove functionality.
        With these, we can always assume a Node's prev and next are non-null values.

        At initialisation:
            [L] <-> [R]
        After entries have been added:
            [L] <-> ... real nodes ... <-> [R]
     */
    private final Node leastRecentlyUsedSentinel;
    private final Node mostRecentlyUsedSentinel;

    public LRUCache(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be > 0");

        this.capacity = capacity;
        map = new HashMap<>();

        leastRecentlyUsedSentinel = new Node();
        mostRecentlyUsedSentinel = new Node();
        leastRecentlyUsedSentinel.next = mostRecentlyUsedSentinel;
        mostRecentlyUsedSentinel.prev = leastRecentlyUsedSentinel;
    }

    // Simply link prev and next, removing node from chain.
    // GC will take care of clean up.
    // Thanks to sentinel nodes, nulls will never happen
    private void remove(Node node) {
        var prev = node.prev;
        var next = node.next;
        prev.next = next;
        next.prev = prev;
    }

    // Always insert at RHS
    // Thanks to sentinels, no null checks are needed.
    // Initial insert is between L & R sentinels, and further are between a real node and R.
    private void insert(Node node) {
        var currMostRecent = mostRecentlyUsedSentinel.prev;
        currMostRecent.next = node;
        node.prev = currMostRecent;
        node.next = mostRecentlyUsedSentinel;
        mostRecentlyUsedSentinel.prev = node;
    }

    // An existing node can be removed from current location and shifted to end in O(1)
    private void makeMostRecent(Node node) {
        remove(node);
        insert(node);
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            var node = map.get(key);
            makeMostRecent(node);
            return node.value;
        }
        return -1;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            // Update key value and make it most recent
            var node = map.get(key);
            node.value = value;
            makeMostRecent(node);
        } else {

            // If we reach capacity, remove the oldest node (from map and DLL)
            if (map.size() >= capacity) {
                map.remove(leastRecentlyUsedSentinel.next.key);
                remove(leastRecentlyUsedSentinel.next); // Safe since we know Cache is not empty
            }

            // Insert new node
            var node = new Node(key, value);
            insert(node);
            map.put(key, node);
        }
    }

    static void main() {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 10);                   // cache: {1=10}
        System.out.println(cache.get(1));   // return 10
        cache.put(2, 20);                   // cache: {1=10, 2=20}
        cache.put(3, 30);                   // cache: {2=20, 3=30}, key=1 was evicted
        System.out.println(cache.get(2));   // returns 20
        System.out.println(cache.get(1));   // return -1 (not found)
    }
}
