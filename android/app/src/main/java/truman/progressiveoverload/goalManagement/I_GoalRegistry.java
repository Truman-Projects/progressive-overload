package truman.progressiveoverload.goalManagement;

import java.util.HashSet;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;

interface I_GoalRegistry<GoalFlavour> extends I_GoalRegistryNotifier<GoalFlavour>,
        I_GoalRegistryUpdater<GoalFlavour> {
    // I_GoalRegistryUpdateSource
    @Override
    void registerListener(I_GoalRegistryListener listener);

    @Override
    void unregisterListener(I_GoalRegistryListener listener);

    @Override
    HashSet<Long> currentGoalIds();

    @Override
    I_GoalNotifier<GoalFlavour> goalUpdateNotifierByGoalId(Long goalId) throws InvalidQueryException;

    // I_GoalRegistryUpdater
    @Override
    I_GoalUpdater<GoalFlavour> goalUpdaterByGoalId(Long goalId) throws InvalidQueryException;

    @Override
    Long addGoal(GoalData<GoalFlavour> goalData);

    @Override
    void removeGoal(Long goalId) throws InvalidQueryException;
}
