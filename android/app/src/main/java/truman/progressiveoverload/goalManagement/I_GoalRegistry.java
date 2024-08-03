package truman.progressiveoverload.goalManagement;

import java.util.HashMap;
import java.util.HashSet;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.measurement.I_TimestampedValue;

interface I_GoalRegistry<TimestampedType extends I_TimestampedValue> extends I_GoalRegistryNotifier<TimestampedType>,
        I_GoalRegistryUpdater<TimestampedType> {
    // I_GoalRegistryUpdateSource
    @Override
    void registerListener(I_GoalRegistryListener listener);

    @Override
    void unregisterListener(I_GoalRegistryListener listener);

    @Override
    HashSet<Long> currentGoalIds();

    @Override
    I_GoalNotifier<TimestampedType> goalUpdateNotifierByGoalId(Long goalId) throws InvalidQueryException;

    // I_GoalRegistryUpdater
    @Override
    I_GoalUpdater<TimestampedType> goalUpdaterByGoalId(Long goalId) throws InvalidQueryException;

    @Override
    Long addGoal(GoalData<TimestampedType> goalData);

    @Override
    void removeGoal(Long goalId) throws InvalidQueryException;
}
