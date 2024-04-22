package truman.progressiveoverload.randomUtilities;

import java.util.concurrent.ThreadLocalRandom;

public class RandomInt implements I_RandomValueGenerator<Integer> {
    public Integer generate(Integer minValue, Integer maxValue) {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue);
    }

    public Integer generate() {
        return generate(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
