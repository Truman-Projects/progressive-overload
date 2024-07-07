package truman.progressiveoverload.measurement.distance;


import truman.progressiveoverload.measurement.I_TimestampedValue;

public class TimestampedDistance implements I_TimestampedValue {
    private final Distance distance_;
    private final long unixTimestampMilliseconds_;

    public TimestampedDistance(Distance distance, long unixTimestampMilliseconds) {
        distance_ = distance;
        unixTimestampMilliseconds_ = unixTimestampMilliseconds;
    }

    @Override
    public long valueInDefaultUnits() {
        return distance_.toCentimeters();
    }

    @Override
    public long unixTimestampMilliseconds() {
        return unixTimestampMilliseconds_;
    }

    public Distance distance() {
        return distance_;
    }
}
