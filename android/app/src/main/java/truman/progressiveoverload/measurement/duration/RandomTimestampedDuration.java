package truman.progressiveoverload.measurement.duration;

import java.time.Duration;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomDuration;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomTimestampedDuration implements I_RandomValueGenerator<TimestampedDuration> {
    public TimestampedDuration generate() {
        Duration randomDuration = new RandomDuration().generate();
        long randomUnixTimestampMilliseconds = new RandomLong().generate();
        return new TimestampedDuration(randomDuration, randomUnixTimestampMilliseconds);
    }
}
