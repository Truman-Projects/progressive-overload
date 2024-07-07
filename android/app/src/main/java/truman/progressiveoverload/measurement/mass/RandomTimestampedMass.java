package truman.progressiveoverload.measurement.mass;


import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomTimestampedMass implements I_RandomValueGenerator<TimestampedMass> {
    public TimestampedMass generate() {
        return new TimestampedMass(new RandomMass().generate(), new RandomLong().generate());
    }
}
