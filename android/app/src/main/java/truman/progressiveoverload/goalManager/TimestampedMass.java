package truman.progressiveoverload.goalManager;

import java.time.LocalDateTime;

import truman.progressiveoverload.measurement.Mass;

public class TimestampedMass implements I_TimestampedValue {
    private final Mass mass_;
    private final LocalDateTime timestamp_;

    public TimestampedMass(Mass mass, LocalDateTime timestamp) {
        mass_ = mass;
        timestamp_ = timestamp;
    }

    @Override
    public long valueInDefaultUnits() {
        return mass_.toMilligrams();
    }

    @Override
    public LocalDateTime timestamp() {
        return timestamp_;
    }

    public Mass mass() {
        return mass_;
    }
}
