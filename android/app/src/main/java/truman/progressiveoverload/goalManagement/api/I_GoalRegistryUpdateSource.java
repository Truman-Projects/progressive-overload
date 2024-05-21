package truman.progressiveoverload.goalManagement.api;

import java.util.HashSet;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalRegistryUpdateSource<TimestampedType extends I_TimestampedValue> {
    void registerListener(I_GoalRegistryUpdateListener<TimestampedType> listener);

    void unregisterListener(I_GoalRegistryUpdateListener<TimestampedType> listener);

    HashSet<String> currentGoalNames();

    I_GoalUpdateNotifier<TimestampedType> goalUpdateNotifierByName(String goalName) throws InvalidQueryException;
}
