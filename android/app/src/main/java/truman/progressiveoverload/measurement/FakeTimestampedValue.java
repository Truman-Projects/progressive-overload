package truman.progressiveoverload.measurement;

import java.time.LocalDateTime;

public class FakeTimestampedValue implements I_TimestampedValue {
    private final long value_;
    private final LocalDateTime timestamp_;

    public FakeTimestampedValue(long value, LocalDateTime timestamp) {
        value_ = value;
        timestamp_ = timestamp;
    }

    public long valueInDefaultUnits() {
        return value_;
    }

    public LocalDateTime timestamp() {
        return timestamp_;
    }
}
