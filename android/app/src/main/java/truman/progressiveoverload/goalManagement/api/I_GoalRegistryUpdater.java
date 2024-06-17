package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalRegistryUpdater<TimestampedType extends I_TimestampedValue> {
    I_GoalUpdater<TimestampedType> goalUpdaterByName(String goalName) throws InvalidQueryException;

    void addGoal(GoalData<TimestampedType> goalData) throws DuplicateEntryException;

    void removeGoal(String goalName) throws InvalidQueryException;
}
