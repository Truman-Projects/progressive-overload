package truman.progressiveoverload.measurement.velocity;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public class TimestampedVelocity implements I_TimestampedValue {
    private final Velocity velocity_;
    private final long unixTimestampMilliseconds_;

    public TimestampedVelocity(Velocity velocity, long unixTimestampMilliseconds) {
        velocity_ = velocity;
        unixTimestampMilliseconds_ = unixTimestampMilliseconds;
    }

    @Override
    public long valueInDefaultUnits() {
        return velocity_.toDistancePerHour().toCentimeters();
    }

    @Override
    public long unixTimestampMilliseconds() {
        return unixTimestampMilliseconds_;
    }

    public Velocity velocity() {
        return velocity_;
    }

}
