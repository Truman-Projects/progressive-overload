package truman.progressiveoverload.randomUtilities;

import java.util.concurrent.ThreadLocalRandom;

public class RandomLong implements I_RandomValueGenerator<Long> {
    public interface RequirementLambda {
        boolean satisfiesRequirement(Long candidate);
    }

    private final RequirementLambda requirement_;

    public RandomLong(RequirementLambda requirement) {
        requirement_ = requirement;
    }

    public RandomLong() {
        requirement_ = (anyLong) -> true;
    }

    public Long generate(Long minValue, Long maxValue) {
        long randomLong = ThreadLocalRandom.current().nextLong(minValue, maxValue);
        while (!requirement_.satisfiesRequirement(randomLong)) {
            randomLong = ThreadLocalRandom.current().nextLong(minValue, maxValue);
        }
        return randomLong;

    }

    public Long generate() {
        return generate(Long.MIN_VALUE, Long.MAX_VALUE);
    }
}
