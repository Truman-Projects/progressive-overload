package truman.progressiveoverload.randomUtilities;

import java.time.Duration;

public class RandomDuration implements I_RandomValueGenerator<Duration> {
    public Duration generate(Long minMilliseconds, Long maxMilliseconds) {
        Long randomMilliseconds = new RandomLong().generate(minMilliseconds, maxMilliseconds);
        return Duration.ofMillis(randomMilliseconds);
    }

    public Duration generate() {
        return generate(Long.MIN_VALUE, Long.MAX_VALUE);
    }
}
