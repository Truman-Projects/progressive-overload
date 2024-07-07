package truman.progressiveoverload.measurement.mass;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public class TimestampedMass implements I_TimestampedValue {
    private final Mass mass_;
    private final long unixTimestampMilliseconds_;

    public TimestampedMass(Mass mass, long unixTimestampMilliseconds) {
        mass_ = mass;
        unixTimestampMilliseconds_ = unixTimestampMilliseconds;
    }

    @Override
    public long valueInDefaultUnits() {
        return mass_.toMilligrams();
    }

    @Override
    public long unixTimestampMilliseconds() {
        return unixTimestampMilliseconds_;
    }

    public Mass mass() {
        return mass_;
    }
}
