package truman.progressiveoverload.measurement.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import truman.progressiveoverload.randomUtilities.RandomLocalDateTime;

class TestTimestampedDistance {
    private Distance distance_;
    private LocalDateTime timestamp_;
    private TimestampedDistance patient_;

    @BeforeEach
    public void resetEverything() {
        distance_ = new RandomDistance().generate();
        timestamp_ = new RandomLocalDateTime().generate();
        patient_ = new TimestampedDistance(distance_, timestamp_);
    }

    @Test
    public void willProvideValueInDefaultUnitsOfCentimeters() {
        assertEquals(distance_.toCentimeters(), patient_.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        assertEquals(timestamp_, patient_.timestamp());
    }

    @Test
    public void willReturnCorrectDistanceObject() {
        assertEquals(distance_, patient_.distance());
    }
}