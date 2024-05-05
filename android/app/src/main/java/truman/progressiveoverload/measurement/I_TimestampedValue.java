package truman.progressiveoverload.measurement;

import java.time.LocalDateTime;

public interface I_TimestampedValue {
    long valueInDefaultUnits();

    LocalDateTime timestamp();
}
