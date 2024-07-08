package truman.progressiveoverload.measurement.fake;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomFakeTimestampedValue implements I_RandomValueGenerator<FakeTimestampedValue> {
    public FakeTimestampedValue generate() {
        RandomLong generator = new RandomLong();
        long randomValue = generator.generate();
        long randomTimestamp = generator.generate();

        return new FakeTimestampedValue(randomValue, randomTimestamp);
    }
}
