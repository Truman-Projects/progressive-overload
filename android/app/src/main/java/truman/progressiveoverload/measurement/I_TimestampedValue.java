package truman.progressiveoverload.measurement;

import java.time.LocalDateTime;

// MUST BE IMMUTABLE (Java moment)
public interface I_TimestampedValue {
    long valueInDefaultUnits();

    LocalDateTime timestamp();
}
