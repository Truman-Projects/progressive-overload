package truman.progressiveoverload.goalManagement.api;

import java.util.HashSet;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalRegistryNotifier<TimestampedType extends I_TimestampedValue> {
    void registerListener(I_GoalRegistryListener listener);

    void unregisterListener(I_GoalRegistryListener listener);

    HashSet<String> currentGoalNames();

    I_GoalNotifier<TimestampedType> goalUpdateNotifierByName(String goalName) throws InvalidQueryException;
}
