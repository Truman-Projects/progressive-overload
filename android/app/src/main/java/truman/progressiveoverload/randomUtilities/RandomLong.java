package truman.progressiveoverload.randomUtilities;

import java.util.concurrent.ThreadLocalRandom;

public class RandomLong implements I_RandomValueGenerator<Long> {
    public Long generate(Long minValue, Long maxValue) {
        return ThreadLocalRandom.current().nextLong(minValue, maxValue);
    }

    public Long generate() {
        return generate(Long.MIN_VALUE, Long.MAX_VALUE);
    }
}
