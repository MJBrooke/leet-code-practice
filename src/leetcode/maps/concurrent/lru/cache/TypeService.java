package leetcode.maps.concurrent.lru.cache;

public class TypeService {

    // Dummy for testing
    public TypeEnum getType(String id) {
        TypeEnum[] values = TypeEnum.values();
        return values[(int) (Math.random() * values.length)];
    }
}
