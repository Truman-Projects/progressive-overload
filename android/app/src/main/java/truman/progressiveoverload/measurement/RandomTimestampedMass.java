package truman.progressiveoverload.measurement;


import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLocalDateTime;

public class RandomTimestampedMass implements I_RandomValueGenerator<TimestampedMass> {
    public TimestampedMass generate() {
        return new TimestampedMass(new RandomMass().generate(), new RandomLocalDateTime().generate());
    }
}
