package truman.progressiveoverload.goalManagement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.measurement.I_TimestampedValue;

class GoalRegistry<TimestampedType extends I_TimestampedValue> implements I_GoalRegistry<TimestampedType> {
    private final I_GoalManagerFactory<TimestampedType> goalManagerFactory_;
    private final UniqueIdSource idSource_;
    private final HashMap<Long, I_GoalManager<TimestampedType>> goalManagersByGoalIds_;
    private Long largestGoalId_;
    private final HashSet<I_GoalRegistryListener> listeners_;

    public GoalRegistry(HashMap<Long, GoalData<TimestampedType>> goalsByIdFromPersistence,
                        I_GoalManagerFactory<TimestampedType> goalManagerFactory, UniqueIdSource uniqueIdSource) {
        goalManagerFactory_ = goalManagerFactory;
        idSource_ = uniqueIdSource;
        goalManagersByGoalIds_ = new HashMap<>();
        for (Map.Entry<Long, GoalData<TimestampedType>> idAndGoalDataPair : goalsByIdFromPersistence.entrySet()) {
            Long id = idAndGoalDataPair.getKey();
            GoalData<TimestampedType> goalData = idAndGoalDataPair.getValue();
            
            boolean idIsAvailable = uniqueIdSource.attemptToReserveId(id);
            if (idIsAvailable) {
                I_GoalManager<TimestampedType> goalManager = goalManagerFactory.createGoalManager(goalData);
                goalManagersByGoalIds_.put(id, goalManager);
            }
        }
        largestGoalId_ = -1L;
        listeners_ = new HashSet<>();
    }

    // I_GoalRegistryNotifier
    @Override
    public void registerListener(I_GoalRegistryListener listener) {
        listeners_.add(listener);
    }

    @Override
    public void unregisterListener(I_GoalRegistryListener listener) {
        listeners_.remove(listener);
    }

    @Override
    public HashSet<Long> currentGoalIds() {
        return new HashSet<>(goalManagersByGoalIds_.keySet());
    }

    @Override
    public I_GoalNotifier<TimestampedType> goalUpdateNotifierByGoalId(Long goalId) throws InvalidQueryException {
        if (!goalManagersByGoalIds_.containsKey(goalId)) {
            throw new InvalidQueryException("No goal update notifier with matching goal ID");
        }
        return goalManagersByGoalIds_.get(goalId);
    }

    // I_GoalRegistryUpdater
    @Override
    public I_GoalUpdater<TimestampedType> goalUpdaterByGoalId(Long goalId) throws InvalidQueryException {
        if (!goalManagersByGoalIds_.containsKey(goalId)) {
            throw new InvalidQueryException("No goal updater with matching goal ID");
        }
        return goalManagersByGoalIds_.get(goalId);
    }

    @Override
    public Long addGoal(GoalData<TimestampedType> goalData) {
        Long newGoalId = idSource_.nextAvailableId();
        goalManagersByGoalIds_.put(newGoalId, goalManagerFactory_.createGoalManager(goalData));
        for (I_GoalRegistryListener listener : listeners_) {
            listener.goalAdded(newGoalId);
        }
        return newGoalId;
    }

    @Override
    public void removeGoal(Long goalId) throws InvalidQueryException {
        if (!goalManagersByGoalIds_.containsKey(goalId)) {
            throw new InvalidQueryException("Attempting to remove non-existent goal ID");
        }
        goalManagersByGoalIds_.remove(goalId);
        for (I_GoalRegistryListener listener : listeners_) {
            listener.goalRemoved(goalId);
        }
    }

    //I_GoalRegistry
    @Override
    public void initializeWithExistingGoals(HashMap<Long, GoalData<TimestampedType>> goalsById) {
        if (largestGoalId_.equals(-1L)) {
            for (Map.Entry<Long, GoalData<TimestampedType>> idAndData : goalsById.entrySet()) {
                Long goalId = idAndData.getKey();
                GoalData<TimestampedType> goalData = idAndData.getValue();
                I_GoalManager<TimestampedType> manager = goalManagerFactory_.createGoalManager(goalData);
                goalManagersByGoalIds_.put(goalId, manager);
                if (goalId > largestGoalId_) {
                    largestGoalId_ = goalId;
                }
            }
        }
    }
}
