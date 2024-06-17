package truman.progressiveoverload.goalManagement;

import java.util.HashSet;

import truman.progressiveoverload.goalManagement.api.DuplicateEntryException;
import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdateListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdateSource;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdateNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.measurement.I_TimestampedValue;

interface I_GoalRegistry<TimestampedType extends I_TimestampedValue> extends I_GoalRegistryUpdateSource<TimestampedType>,
        I_GoalRegistryUpdater<TimestampedType> {
    // I_GoalRegistryUpdateSource
    @Override
    void registerListener(I_GoalRegistryUpdateListener<TimestampedType> listener);

    @Override
    void unregisterListener(I_GoalRegistryUpdateListener<TimestampedType> listener);

    @Override
    HashSet<String> currentGoalNames();

    @Override
    I_GoalUpdateNotifier<TimestampedType> goalUpdateNotifierByName(String goalName) throws InvalidQueryException;

    // I_GoalRegistryUpdater
    @Override
    I_GoalUpdater<TimestampedType> goalUpdaterByName(String goalName) throws InvalidQueryException;

    @Override
    void addGoal(GoalData<TimestampedType> goalData) throws DuplicateEntryException;

    @Override
    void removeGoal(String goalName) throws InvalidQueryException;

}
