package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalRegistryUpdateListener<TimestampedType extends I_TimestampedValue> {
    void goalAdded(String goalName);

    void goalRemoved(String goalName);
}
