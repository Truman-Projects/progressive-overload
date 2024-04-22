package truman.progressiveoverload.randomUtilities;

import java.util.concurrent.ThreadLocalRandom;

public class RandomDouble implements I_RandomValueGenerator<Double> {
    public Double generate(Double minValue, Double maxValue) {
        return ThreadLocalRandom.current().nextDouble(minValue, maxValue);
    }

    public Double generate() {
        return generate(Double.MIN_VALUE, Double.MAX_VALUE);
    }
}
