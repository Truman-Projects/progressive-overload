package truman.progressiveoverload.measurement.duration;

import java.time.Duration;
import java.time.LocalDateTime;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public class TimestampedDuration implements I_TimestampedValue {
    private final Duration duration_;
    private final LocalDateTime timestamp_;

    public TimestampedDuration(Duration duration, LocalDateTime timestamp) {
        duration_ = duration;
        timestamp_ = timestamp;
    }

    @Override
    public long valueInDefaultUnits() {
        return duration_.toMillis();
    }

    @Override
    public LocalDateTime timestamp() {
        return timestamp_;
    }

    public Duration duration() {
        return duration_;
    }
}
