package truman.progressiveoverload.randomUtilities;

import java.util.ArrayList;
import java.util.HashMap;

public class RandomHashMap<K, V> implements I_RandomValueGenerator<HashMap<K, V>> {
    private static final int DEFAULT_SIZE = 3;
    private final RandomOther<K> uniqueKeyGenerator_;
    private final I_RandomValueGenerator<V> valueGenerator_;

    public RandomHashMap(I_RandomValueGenerator<K> keyGenerator, I_RandomValueGenerator<V> valueGenerator) {

        uniqueKeyGenerator_ = new RandomOther<>(keyGenerator);
        valueGenerator_ = valueGenerator;
    }

    public HashMap<K, V> generate(int size) {
        HashMap<K, V> map = new HashMap<>();
        ArrayList<K> currentKeys = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            K key = uniqueKeyGenerator_.otherThan(currentKeys);
            V value = valueGenerator_.generate();
            map.put(key, value);
        }
        return map;
    }

    public HashMap<K, V> generate() {
        return generate(DEFAULT_SIZE);
    }
}
