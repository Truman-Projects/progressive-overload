package truman.progressiveoverload.measurement.duration;

import java.time.Duration;
import java.time.LocalDateTime;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomDuration;
import truman.progressiveoverload.randomUtilities.RandomLocalDateTime;

public class RandomTimestampedDuration implements I_RandomValueGenerator<TimestampedDuration> {
    public TimestampedDuration generate() {
        Duration randomDuration = new RandomDuration().generate();
        LocalDateTime randomTimestamp = new RandomLocalDateTime().generate();
        return new TimestampedDuration(randomDuration, randomTimestamp);
    }
}
