package truman.progressiveoverload.measurement.custom;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import truman.progressiveoverload.randomUtilities.RandomDouble;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestTimestampedCustomValue {
    private double value_;
    private long unixTimestampMilliseconds_;
    private TimestampedCustomValue patient_;

    @BeforeEach
    public void resetEverything() {
        value_ = new RandomDouble().generate();
        unixTimestampMilliseconds_ = new RandomLong().generate();
        patient_ = new TimestampedCustomValue(value_, unixTimestampMilliseconds_);
    }

    @Test
    public void willProvideValueInDefaultUnitsOfHundredths() {
        long valueWithHundredthPrecision = (long) (value_ * 100);
        assertEquals(valueWithHundredthPrecision, patient_.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        assertEquals(unixTimestampMilliseconds_, patient_.unixTimestampMilliseconds());
    }

    @Test
    public void willReturnCorrectValue() {
        assertEquals(value_, patient_.getValue());
    }
}