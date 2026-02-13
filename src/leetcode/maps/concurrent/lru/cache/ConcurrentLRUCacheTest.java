package leetcode.maps.concurrent.lru.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrentLRUCacheTest {
    private ConcurrentLRUCache cache;
    private MockTypeService mockService;

    @BeforeEach
    public void setUp() {
        mockService = new MockTypeService();
        // Initialize with a small capacity to force frequent evictions
        cache = new ConcurrentLRUCache(10, mockService);
    }

    /**
     * TEST 1: The "Thundering Herd" (Validating computeIfAbsent)
     * Proves that if 100 threads request the SAME new ID simultaneously,
     * the underlying external service is only called EXACTLY once.
     */
    @Test
    public void testThunderingHerdOnSingleKey() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        String targetId = "1234";

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                readyLatch.countDown(); // Report ready
                try {
                    startLatch.await(); // Wait for the green light
                    cache.getOutputType(targetId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown(); // Report finished
                }
            });
        }

        readyLatch.await(); // Wait until all threads are created and ready
        startLatch.countDown(); // Release the hounds! (All 100 threads hit the cache instantly)
        doneLatch.await(); // Wait for all threads to finish

        // ASSERTION: The service was only invoked once for this ID, proving atomicity.
        assertEquals(1, mockService.getInvocationCount(targetId));
        assertEquals(1, cache.getSize());
        executor.shutdown();
    }

    /**
     * TEST 2: Concurrent Capacity and Eviction
     * Proves that under heavy, chaotic insertion, the cache NEVER exceeds maxCapacity,
     * and pointers do not get corrupted (no NullPointerExceptions).
     */
    @Test
    public void testCapacityUnderConcurrentLoad() throws InterruptedException {
        int threadCount = 200;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int id = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    // Each thread requests a unique ID, forcing 200 insertions and 190 evictions
                    cache.getOutputType("ID_" + id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();

        // ASSERTION: The cache strictly enforced the maxCapacity of 10.
        assertEquals(10, cache.getSize());

        // ASSERTION: The external service was called 200 times total (once per unique ID).
        assertEquals(200, mockService.getTotalInvocations());
        executor.shutdown();
    }

    /**
     * TEST 3: Concurrent Reads and Writes (Deadlock & Race Condition Check)
     * Simulates a real-world scenario where some threads are reading hot data,
     * while others are triggering evictions with cold data.
     */
    @Test
    public void testMixedReadWriteConcurrency() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    // 80% of requests hit the same 5 "hot" keys (forcing moveToMostRecentlyUsed)
                    // 20% of requests hit unique "cold" keys (forcing inserts and evictions)
                    if (threadId % 5 != 0) {
                        cache.getOutputType("HOT_BIN_" + (threadId % 5));
                    } else {
                        cache.getOutputType("COLD_BIN_" + threadId);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();

        // ASSERTION: As long as it finishes without hanging, we prove there are no deadlocks.
        // ASSERTION: Capacity remains intact.
        assertEquals(10, cache.getSize());
        executor.shutdown();
    }

    // --- Mock Service for tracking invocations ---

    private static class MockTypeService extends TypeService {
        private final ConcurrentHashMap<String, AtomicInteger> invocationCounts = new ConcurrentHashMap<>();
        private final AtomicInteger totalInvocations = new AtomicInteger(0);

        @Override
        public TypeEnum getType(String id) {
            invocationCounts.computeIfAbsent(id, k -> new AtomicInteger(0)).incrementAndGet();
            totalInvocations.incrementAndGet();

            // Simulate a slow external network call (crucial for exposing race conditions!)
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return TypeEnum.A;
        }

        public int getInvocationCount(String id) {
            return invocationCounts.getOrDefault(id, new AtomicInteger(0)).get();
        }

        public int getTotalInvocations() {
            return totalInvocations.get();
        }
    }
}