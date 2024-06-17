package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalRegistryUpdater<TimestampedType extends I_TimestampedValue> {
    I_GoalUpdater<TimestampedType> goalUpdaterByGoalId(Long goalId) throws InvalidQueryException;

    Long addGoal(GoalData<TimestampedType> goalData);

    void removeGoal(Long goalId) throws InvalidQueryException;
}
