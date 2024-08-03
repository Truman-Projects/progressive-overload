package truman.progressiveoverload.goalManagement;


import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.measurement.I_TimestampedValue;

public class GoalRegistryFactory<TimestampedType extends I_TimestampedValue> implements I_GoalRegistryFactory<TimestampedType> {
    private final I_GoalDataPersistenceSource<TimestampedType> persistenceSource_;
    private final I_GoalManagerFactory<TimestampedType> goalManagerFactory_;
    private final UniqueIdSource idSource_;

    public GoalRegistryFactory(I_GoalDataPersistenceSource<TimestampedType> persistenceSource,
                               I_GoalManagerFactory<TimestampedType> goalManagerFactory, UniqueIdSource idSource) {
        persistenceSource_ = persistenceSource;
        goalManagerFactory_ = goalManagerFactory;
        idSource_ = idSource;
    }

    @Override
    public I_GoalRegistry<TimestampedType> createGoalRegistry() {
        return new GoalRegistry<>(persistenceSource_.loadGoalDataFromMemory(), goalManagerFactory_, idSource_);
    }
}
