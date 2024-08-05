package truman.progressiveoverload.goalManagement;


import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;

public class GoalRegistryFactory<GoalFlavour> implements I_GoalRegistryFactory<GoalFlavour> {
    private final I_GoalDataPersistenceSource<GoalFlavour> persistenceSource_;
    private final I_GoalManagerFactory<GoalFlavour> goalManagerFactory_;
    private final UniqueIdSource idSource_;

    public GoalRegistryFactory(I_GoalDataPersistenceSource<GoalFlavour> persistenceSource,
                               I_GoalManagerFactory<GoalFlavour> goalManagerFactory, UniqueIdSource idSource) {
        persistenceSource_ = persistenceSource;
        goalManagerFactory_ = goalManagerFactory;
        idSource_ = idSource;
    }

    @Override
    public I_GoalRegistry<GoalFlavour> createGoalRegistry() {
        return new GoalRegistry<>(persistenceSource_.loadGoalDataFromMemory(), goalManagerFactory_, idSource_);
    }
}
