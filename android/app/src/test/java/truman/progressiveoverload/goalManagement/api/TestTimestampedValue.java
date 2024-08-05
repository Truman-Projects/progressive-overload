package truman.progressiveoverload.goalManagement.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import truman.progressiveoverload.randomUtilities.RandomLong;

class TestTimestampedValue {

    private long unixTimestampMilliseconds_;
    private Long value_;
    private TimestampedValue<Long> patient_;

    @BeforeEach
    public void resetEverything() {
        RandomLong generator = new RandomLong();
        unixTimestampMilliseconds_ = generator.generate();
        value_ = generator.generate();
        patient_ = new TimestampedValue<>(unixTimestampMilliseconds_, value_);
    }

    @Test
    public void willReturnCorrectTimestamp() {
        assertEquals(unixTimestampMilliseconds_, patient_.unixTimestampMilliseconds());
    }

    @Test
    public void willReturnCorrectValue() {
        assertEquals(value_, patient_.value());
    }
}