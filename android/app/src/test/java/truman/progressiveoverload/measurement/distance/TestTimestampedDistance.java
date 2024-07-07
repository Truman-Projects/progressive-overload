package truman.progressiveoverload.measurement.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import truman.progressiveoverload.randomUtilities.RandomLong;

class TestTimestampedDistance {
    private Distance distance_;
    private long unixTimestampMilliseconds_;
    private TimestampedDistance patient_;

    @BeforeEach
    public void resetEverything() {
        distance_ = new RandomDistance().generate();
        unixTimestampMilliseconds_ = new RandomLong().generate();
        patient_ = new TimestampedDistance(distance_, unixTimestampMilliseconds_);
    }

    @Test
    public void willProvideValueInDefaultUnitsOfCentimeters() {
        assertEquals(distance_.toCentimeters(), patient_.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        assertEquals(unixTimestampMilliseconds_, patient_.unixTimestampMilliseconds());
    }

    @Test
    public void willReturnCorrectDistanceObject() {
        assertEquals(distance_, patient_.distance());
    }
}