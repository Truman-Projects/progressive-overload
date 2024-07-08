package truman.progressiveoverload.measurement.custom;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomDouble;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomTimestampedCustomValue implements I_RandomValueGenerator<TimestampedCustomValue> {
    @Override
    public TimestampedCustomValue generate() {
        double randomValue = new RandomDouble().generate();
        long randomUnixTimestampMilliseconds = new RandomLong().generate();

        return new TimestampedCustomValue(randomValue, randomUnixTimestampMilliseconds);
    }
}
