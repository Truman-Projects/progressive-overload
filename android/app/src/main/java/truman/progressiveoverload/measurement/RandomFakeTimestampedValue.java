package truman.progressiveoverload.measurement;

import java.time.LocalDateTime;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLocalDateTime;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomFakeTimestampedValue implements I_RandomValueGenerator<FakeTimestampedValue> {
    public FakeTimestampedValue generate() {
        long randomValue = new RandomLong().generate();
        LocalDateTime randomDateTime = new RandomLocalDateTime().generate();
        return new FakeTimestampedValue(randomValue, randomDateTime);
    }
}
