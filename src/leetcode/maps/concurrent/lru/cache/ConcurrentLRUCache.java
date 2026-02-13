package leetcode.maps.concurrent.lru.cache;

/*
Skeleton of the service you need to complete

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
    A, B, C
}
 */
public class ConcurrentLRUCache {
    private final int maxCapacity = 100;
    private final TypeService typeService;

    public ConcurrentLRUCache() {
        typeService = new TypeService();
    }

    public TypeEnum getOutputType(String id) {
        return null;
    }
}
