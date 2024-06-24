package truman.progressiveoverload.measurement.distance;


import java.time.LocalDateTime;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLocalDateTime;

public class RandomTimestampedDistance implements I_RandomValueGenerator<TimestampedDistance> {
    public TimestampedDistance generate() {
        Distance randomDistance = new RandomDistance().generate();
        LocalDateTime randomTimestamp = new RandomLocalDateTime().generate();
        return new TimestampedDistance(randomDistance, randomTimestamp);
    }
}
