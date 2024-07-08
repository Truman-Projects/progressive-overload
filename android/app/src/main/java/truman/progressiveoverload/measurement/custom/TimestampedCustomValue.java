package truman.progressiveoverload.measurement.custom;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public class TimestampedCustomValue implements I_TimestampedValue {
    private final double value_;
    private final long unixTimestampMilliseconds_;

    public TimestampedCustomValue(double value, long unixTimestampMilliseconds) {
        value_ = value;
        unixTimestampMilliseconds_ = unixTimestampMilliseconds;
    }

    @Override
    public long valueInDefaultUnits() {
        // IMPORTANT: returns value multiplied by 100 as a long to preserve some decimal precision
        return (long) (value_ * 100);
    }

    @Override
    public long unixTimestampMilliseconds() {
        return unixTimestampMilliseconds_;
    }

    public double getValue() {
        return value_;
    }

}
