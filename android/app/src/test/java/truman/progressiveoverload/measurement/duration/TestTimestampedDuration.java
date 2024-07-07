package truman.progressiveoverload.measurement.duration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import truman.progressiveoverload.randomUtilities.RandomDuration;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestTimestampedDuration {
    private Duration duration_;
    private long unixTimestampMilliseconds_;
    private TimestampedDuration patient_;

    @BeforeEach
    public void resetEverything() {
        duration_ = new RandomDuration().generate();
        unixTimestampMilliseconds_ = new RandomLong().generate();
        patient_ = new TimestampedDuration(duration_, unixTimestampMilliseconds_);
    }

    @Test
    public void willProvideValueInDefaultUnitsOfMilliseconds() {
        assertEquals(duration_.toMillis(), patient_.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        assertEquals(unixTimestampMilliseconds_, patient_.unixTimestampMilliseconds());
    }

    @Test
    public void willReturnCorrectDurationObject() {
        assertEquals(duration_, patient_.duration());
    }

}