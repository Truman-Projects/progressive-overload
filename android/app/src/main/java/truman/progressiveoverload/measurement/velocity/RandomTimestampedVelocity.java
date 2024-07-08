package truman.progressiveoverload.measurement.velocity;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomTimestampedVelocity implements I_RandomValueGenerator<TimestampedVelocity> {
    public TimestampedVelocity generate() {
        Velocity randomVelocity = new RandomVelocity().generate();
        long randomUnixTimestampMilliseconds = new RandomLong().generate();
        return new TimestampedVelocity(randomVelocity, randomUnixTimestampMilliseconds);
    }
}
