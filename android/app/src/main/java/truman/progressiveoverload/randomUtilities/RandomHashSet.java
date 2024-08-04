package truman.progressiveoverload.randomUtilities;

import java.util.HashSet;

public class RandomHashSet<T> implements I_RandomValueGenerator<HashSet<T>> {
    private static final int DEFAULT_SIZE = 3;
    private final I_RandomValueGenerator<T> generator_;

    public RandomHashSet(I_RandomValueGenerator<T> generator) {
        generator_ = generator;
    }

    public HashSet<T> generate(int size) {
        HashSet<T> set = new HashSet<>();
        while (set.size() < size) {
            T candidate = generator_.generate();
            set.add(candidate);
        }
        return set;
    }

    @Override
    public HashSet<T> generate() {
        return generate(DEFAULT_SIZE);
    }
}
