package truman.progressiveoverload.measurement.velocity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import truman.progressiveoverload.measurement.distance.Distance;
import truman.progressiveoverload.measurement.distance.RandomDistance;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestTimestampedVelocity {
    private Distance distancePerHour_;
    private Velocity velocity_;
    private long unixTimestampMilliseconds_;
    private TimestampedVelocity patient_;

    @BeforeEach
    public void resetEverything() {
        distancePerHour_ = new RandomDistance().generate();
        velocity_ = Velocity.fromDistancePerHour(distancePerHour_);
        unixTimestampMilliseconds_ = new RandomLong().generate();
        patient_ = new TimestampedVelocity(velocity_, unixTimestampMilliseconds_);
    }

    @Test
    public void willProvideValueInDefaultUnitsOfCentimetersPerHour() {
        assertEquals(distancePerHour_.toCentimeters(), patient_.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        assertEquals(unixTimestampMilliseconds_, patient_.unixTimestampMilliseconds());
    }

    @Test
    public void willReturnCorrectVelocityObject() {
        assertEquals(velocity_, patient_.velocity());
    }

}