package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalManagementModule<TimestampedType extends I_TimestampedValue> {
    I_GoalRegistryNotifier<TimestampedType> goalRegistryNotifier();

    I_GoalRegistryUpdater<TimestampedType> goalRegistryUpdater();
}
