package truman.progressiveoverload.measurement;

public class FakeTimestampedValue implements I_TimestampedValue {
    private final long value_;
    private final long unixTimestampMilliseconds_;

    public FakeTimestampedValue(long value, Long unixTimestampMilliseconds) {
        value_ = value;
        unixTimestampMilliseconds_ = unixTimestampMilliseconds;
    }

    public long valueInDefaultUnits() {
        return value_;
    }

    public long unixTimestampMilliseconds() {
        return unixTimestampMilliseconds_;
    }
}
