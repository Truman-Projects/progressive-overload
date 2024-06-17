package truman.progressiveoverload.goalManagement;

import java.util.HashMap;
import java.util.HashSet;

import truman.progressiveoverload.goalManagement.api.DuplicateEntryException;
import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdateListener;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdateNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.measurement.I_TimestampedValue;

class GoalRegistry<TimestampedType extends I_TimestampedValue> implements I_GoalRegistry<TimestampedType> {
    private final I_GoalManagerFactory<TimestampedType> goalManagerFactory_;
    private final HashMap<String, I_GoalManager<TimestampedType>> goalManagersByGoalNames_;
    private final HashSet<I_GoalRegistryUpdateListener<TimestampedType>> listeners_;

    public GoalRegistry(
            I_GoalManagerFactory<TimestampedType> goalManagerFactory) {
        goalManagerFactory_ = goalManagerFactory;
        goalManagersByGoalNames_ = new HashMap<>();
        listeners_ = new HashSet<>();
    }

    @Override
    public void registerListener(I_GoalRegistryUpdateListener<TimestampedType> listener) {
        listeners_.add(listener);
    }

    @Override
    public void unregisterListener(I_GoalRegistryUpdateListener<TimestampedType> listener) {
        listeners_.remove(listener);
    }

    @Override
    public HashSet<String> currentGoalNames() {
        return new HashSet<>(goalManagersByGoalNames_.keySet());
    }

    @Override
    public I_GoalUpdateNotifier<TimestampedType> goalUpdateNotifierByName(String goalName) throws InvalidQueryException {
        if (!goalManagersByGoalNames_.containsKey(goalName)) {
            throw new InvalidQueryException("No goal update notifier with matching goal name");
        }
        return goalManagersByGoalNames_.get(goalName);
    }


    @Override
    public I_GoalUpdater<TimestampedType> goalUpdaterByName(String goalName) throws InvalidQueryException {
        if (!goalManagersByGoalNames_.containsKey(goalName)) {
            throw new InvalidQueryException("No goal updater with matching goal name");
        }
        return goalManagersByGoalNames_.get(goalName);
    }

    @Override
    public void addGoal(GoalData<TimestampedType> goalData) throws DuplicateEntryException {
        String goalName = goalData.name();
        if (goalManagersByGoalNames_.containsKey(goalName)) {
            throw new DuplicateEntryException("Attempting to add goal with existing goal name");
        }
        goalManagersByGoalNames_.put(goalName, goalManagerFactory_.createGoalManager(goalData));
        for (I_GoalRegistryUpdateListener<TimestampedType> listener : listeners_) {
            listener.goalAdded(goalName);
        }
    }

    @Override
    public void removeGoal(String goalName) throws InvalidQueryException {
        if (!goalManagersByGoalNames_.containsKey(goalName)) {
            throw new InvalidQueryException("Attempting to remove non-existent goal name");
        }
        goalManagersByGoalNames_.remove(goalName);
        for (I_GoalRegistryUpdateListener<TimestampedType> listener : listeners_) {
            listener.goalRemoved(goalName);
        }
    }
}
