package truman.progressiveoverload.measurement.distance;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomTimestampedDistance implements I_RandomValueGenerator<TimestampedDistance> {
    public TimestampedDistance generate() {
        Distance randomDistance = new RandomDistance().generate();
        long randomUnixTimestampMilliseconds = new RandomLong().generate();
        return new TimestampedDistance(randomDistance, randomUnixTimestampMilliseconds);
    }
}
