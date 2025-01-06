package truman.progressiveoverload.goalManagement.api;

import javarandoms.I_RandomValueGenerator;
import javarandoms.RandomLong;

public class RandomTimestampedValue<T> implements I_RandomValueGenerator<TimestampedValue<T>> {
    private final RandomLong timestampGenerator_;
    private final I_RandomValueGenerator<T> valueGenerator;

    public RandomTimestampedValue(I_RandomValueGenerator<T> generator) {
        timestampGenerator_ = new RandomLong();
        valueGenerator = generator;
    }

    @Override
    public TimestampedValue<T> generate() {
        long randomUnixTimestampMilliseconds = timestampGenerator_.generate();
        T randomValue = valueGenerator.generate();
        return new TimestampedValue<>(randomUnixTimestampMilliseconds, randomValue);
    }
}
