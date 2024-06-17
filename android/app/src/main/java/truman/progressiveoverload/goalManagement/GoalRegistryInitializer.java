package truman.progressiveoverload.goalManagement;

import java.util.HashMap;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.measurement.I_TimestampedValue;

class GoalRegistryInitializer<TimestampedType extends I_TimestampedValue> {
    private final I_GoalDataPersistenceSource<TimestampedType> persistenceSource_;
    private final I_GoalRegistryFactory<TimestampedType> goalRegistryFactory_;

    public GoalRegistryInitializer(I_GoalDataPersistenceSource<TimestampedType> persistenceSource,
                                   I_GoalRegistryFactory<TimestampedType> goalRegistryFactory
    ) {
        persistenceSource_ = persistenceSource;
        goalRegistryFactory_ = goalRegistryFactory;
    }

    public I_GoalRegistry<TimestampedType> initializeGoalRegistry() {
        I_GoalRegistry<TimestampedType> goalRegistry = goalRegistryFactory_.createGoalRegistry();
        HashMap<Long, GoalData<TimestampedType>> persistenceData = persistenceSource_.loadGoalDataFromMemory();
        goalRegistry.initializeWithExistingGoals(persistenceData);
        return goalRegistry;
    }
}
