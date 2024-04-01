package truman.progressiveoverload.goalManager;

import java.time.LocalDateTime;

interface I_TimestampedValue {
    long valueInDefaultUnits();

    LocalDateTime timestamp();
}
