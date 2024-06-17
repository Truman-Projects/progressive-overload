package truman.progressiveoverload.goalManagement;

import java.util.HashSet;

import truman.progressiveoverload.goalManagement.api.DuplicateEntryException;
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
    HashSet<String> currentGoalNames();

    @Override
    I_GoalNotifier<TimestampedType> goalUpdateNotifierByName(String goalName) throws InvalidQueryException;

    // I_GoalRegistryUpdater
    @Override
    I_GoalUpdater<TimestampedType> goalUpdaterByName(String goalName) throws InvalidQueryException;

    @Override
    void addGoal(GoalData<TimestampedType> goalData) throws DuplicateEntryException;

    @Override
    void removeGoal(String goalName) throws InvalidQueryException;

}
