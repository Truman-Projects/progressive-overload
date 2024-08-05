package truman.progressiveoverload.goalManagement.api;

// Must remain immutable.  Type T must also be immutable (java moment)
public class TimestampedValue<T> {
    private final long unixTimestampMilliseconds_;
    private final T value_;

    public TimestampedValue(long unixTimestampMilliseconds, T value) {
        unixTimestampMilliseconds_ = unixTimestampMilliseconds;
        value_ = value;
    }

    public long unixTimestampMilliseconds() {
        return unixTimestampMilliseconds_;
    }

    public T value() {
        return value_;
    }
}