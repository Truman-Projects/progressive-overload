package truman.progressiveoverload.goalManagement;

import java.util.ArrayList;

import truman.progressiveoverload.goalManagement.api.DuplicateEntryException;
import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.measurement.I_TimestampedValue;

// scuffed factory pattern; if this becomes problematic we can make an actual GoalRegistryFactory in the future
class GoalRegistryInitializer<TimestampedType extends I_TimestampedValue> {
    private final I_GoalDataPersistenceSource<TimestampedType> persistenceSource_;
    private final I_GoalManagerFactory<TimestampedType> goalManagerFactory_;

    public GoalRegistryInitializer(I_GoalDataPersistenceSource<TimestampedType> persistenceSource,
                                   I_GoalManagerFactory<TimestampedType> goalManagerFactory) {
        persistenceSource_ = persistenceSource;
        goalManagerFactory_ = goalManagerFactory;
    }

    public I_GoalRegistry<TimestampedType> createGoalRegistry() {
        ArrayList<GoalData<TimestampedType>> goalsFromPersistence = persistenceSource_.loadGoalDataFromMemory();
        GoalRegistry<TimestampedType> goalRegistry = new GoalRegistry<>(goalManagerFactory_);
        for (GoalData<TimestampedType> goal : goalsFromPersistence) {
            try {
                goalRegistry.addGoal(goal);
            } catch (DuplicateEntryException ignored) {
            }
        }
        return goalRegistry;
    }
}
