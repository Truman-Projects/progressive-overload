package truman.progressiveoverload.goalManagement;

import java.time.LocalDateTime;

interface I_TimestampedValue {
    long valueInDefaultUnits();

    LocalDateTime timestamp();
}
