package leetcode.maps.concurrent.lru.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static leetcode.maps.concurrent.lru.cache.TypeEnum.UNKNOWN;

/*
Skeleton of the service you need to complete.
If the key requested is not in the cache, look it up with typeService.

public class ConcurrentLRUCache {
    private final int maxCapacity = 100;
    private final TypeService typeService;

    // TODO: Define your thread-safe cache here

    public ConcurrentLRUCache(TypeService typeService) {
        this.typeService = typeService;
    }

    public OutputType getOutputType(String id) {
        // TODO: Implement the caching logic
        return null;
    }
}

// The external registry (assume implemented elsewhere)
interface TypeService {
    OutputType lookup(String id);
}

enum OutputType {
    A, B, C, UNKNOWN
}
 */
public class ConcurrentLRUCache {
    private final TypeService typeService;

    // The queue tracks access order for eviction
    private final LRUQueue queue;

    // The map provides O(1) access and thread-safe insert and retrieval
    private final Map<String, Node> cache;

    // Granular lock for just the LRU mutations
    private final Object queueLock;

    private final int maxCapacity;
    private int currentSize;

    public ConcurrentLRUCache(int maxCapacity, TypeService typeService) {
        if (maxCapacity <= 0)
            throw new IllegalArgumentException("Capacity of cache must be greater than zero");
        this.maxCapacity = maxCapacity;

        this.typeService = Objects.requireNonNull(typeService, "typeService cannot be null");

        currentSize = 0;
        queueLock = new Object();
        queue = new LRUQueue();
        cache = new ConcurrentHashMap<>();
    }

    public TypeEnum getOutputType(String id) {

        // Atomic get/put
        // Prevents multiple threads making a request for the same ID.
        // The underlying striped implementation ensures that only that underlying bucket
        //  is locked, but calls to other buckets remain unblocked.
        Node node = cache.computeIfAbsent(id, key -> {
            TypeEnum type = typeService.getType(key);
            return new Node(key, type);
        });

        // Synchronized mutation of LRU ordering
        synchronized (queueLock) {
            // If the Node has been seen before and was in the cache, prev will never be null
            //  due to the sentinel node design
            if (node.prev == null) {
                if (currentSize == maxCapacity) {
                    var lru = queue.removeLeastRecentlyUsed();
                    cache.remove(lru.id);
                    currentSize--;
                }
                queue.insert(node);
                currentSize++;
            } else {
                // If already in the queue, simply move it to the MRU position
                queue.moveToMostRecentlyUsed(node);
            }
        }

        return node.type;
    }

    public int getSize() {
        return currentSize;
    }

    // Private implementation classes

    private static class Node {
        String id;
        TypeEnum type;
        Node prev;
        Node next;

        public Node(String id, TypeEnum type) {
            this.id = id;
            this.type = type;
        }

        // Sentinel constructor
        public Node() {
            this("SENTINEL", UNKNOWN);
        }
    }

    /*
    LRU Queue is a doubly-linked list that tracks how recently each Node has been fetched or input.
    It keeps two sentinel nodes as the permanent head and tail to simplify logic, so that any given Node
        other than these two will always have a prev and next that are not null.
     */
    private static class LRUQueue {
        private final Node lruSentinel;
        private final Node mruSentinel;

        public LRUQueue() {
            lruSentinel = new Node();
            mruSentinel = new Node();
            lruSentinel.next = mruSentinel;
            mruSentinel.prev = lruSentinel;
        }

        public Node removeLeastRecentlyUsed() {
            var lruNode = lruSentinel.next;
            remove(lruSentinel.next);
            return lruNode;
        }

        public void moveToMostRecentlyUsed(Node node) {
            remove(node);
            insert(node);
        }

        private void remove(Node node) {
            var prev = node.prev;
            var next = node.next;
            prev.next = next;
            next.prev = prev;
            node.prev = null;
            node.next = null;
        }

        public void insert(Node node) {
            var prevMostRecent = mruSentinel.prev;

            prevMostRecent.next = node;
            node.prev = prevMostRecent;
            node.next = mruSentinel;
            mruSentinel.prev = node;
        }
    }
}
