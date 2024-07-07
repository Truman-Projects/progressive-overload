package truman.progressiveoverload.measurement.duration;

import java.time.Duration;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public class TimestampedDuration implements I_TimestampedValue {
    private final Duration duration_;
    private final long unixTimestampMilliseconds_;

    public TimestampedDuration(Duration duration, long unixTimestampMilliseconds) {
        duration_ = duration;
        unixTimestampMilliseconds_ = unixTimestampMilliseconds;
    }

    @Override
    public long valueInDefaultUnits() {
        return duration_.toMillis();
    }

    @Override
    public long unixTimestampMilliseconds() {
        return unixTimestampMilliseconds_;
    }

    public Duration duration() {
        return duration_;
    }
}
