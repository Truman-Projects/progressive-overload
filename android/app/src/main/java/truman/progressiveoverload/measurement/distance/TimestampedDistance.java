package truman.progressiveoverload.measurement.distance;


import java.time.LocalDateTime;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public class TimestampedDistance implements I_TimestampedValue {
    private final Distance distance_;
    private final LocalDateTime timestamp_;

    public TimestampedDistance(Distance distance, LocalDateTime timestamp) {
        distance_ = distance;
        timestamp_ = timestamp;
    }

    @Override
    public long valueInDefaultUnits() {
        return distance_.toCentimeters();
    }

    @Override
    public LocalDateTime timestamp() {
        return timestamp_;
    }

    public Distance distance() {
        return distance_;
    }
}
