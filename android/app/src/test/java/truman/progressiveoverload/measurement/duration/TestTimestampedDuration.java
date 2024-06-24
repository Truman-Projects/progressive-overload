package truman.progressiveoverload.measurement.duration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import truman.progressiveoverload.randomUtilities.RandomDuration;
import truman.progressiveoverload.randomUtilities.RandomLocalDateTime;

class TestTimestampedDuration {
    private Duration duration_;
    private LocalDateTime timestamp_;
    private TimestampedDuration patient_;

    @BeforeEach
    public void resetEverything() {
        duration_ = new RandomDuration().generate();
        timestamp_ = new RandomLocalDateTime().generate();
        patient_ = new TimestampedDuration(duration_, timestamp_);
    }

    @Test
    public void willProvideValueInDefaultUnitsOfMilliseconds() {
        assertEquals(duration_.toMillis(), patient_.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        assertEquals(timestamp_, patient_.timestamp());
    }

    @Test
    public void willReturnCorrectDurationObject() {
        assertEquals(duration_, patient_.duration());
    }

}