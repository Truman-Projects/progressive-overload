package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalRegistryFactory<TimestampedType extends I_TimestampedValue> {
    I_GoalRegistry<TimestampedType> createGoalRegistry();
}
