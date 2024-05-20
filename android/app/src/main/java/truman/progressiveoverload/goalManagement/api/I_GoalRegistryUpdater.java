package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalRegistryUpdater<TimestampedType extends I_TimestampedValue> {
    I_GoalUpdater<TimestampedType> goalUpdaterByName(String goalName);

    void addGoal(GoalData<TimestampedType> goalData);

    void removeGoal(String goalName);
}
