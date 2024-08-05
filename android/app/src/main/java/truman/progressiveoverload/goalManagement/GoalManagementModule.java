package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.I_GoalManagementModule;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;

public class GoalManagementModule<GoalFlavour> implements I_GoalManagementModule<GoalFlavour> {
    private final I_GoalRegistry<GoalFlavour> goalRegistry_;

    public GoalManagementModule(I_GoalDataPersistenceSource<GoalFlavour> persistenceSource, UniqueIdSource idSource) {
        GoalManagerFactory<GoalFlavour> goalManagerFactory = new GoalManagerFactory<>();
        GoalRegistryFactory<GoalFlavour> goalRegistryFactory = new GoalRegistryFactory<>(persistenceSource, goalManagerFactory,
                idSource);
        goalRegistry_ = goalRegistryFactory.createGoalRegistry();
    }

    @Override
    public I_GoalRegistryNotifier<GoalFlavour> goalRegistryNotifier() {
        return goalRegistry_;
    }

    @Override
    public I_GoalRegistryUpdater<GoalFlavour> goalRegistryUpdater() {
        return goalRegistry_;
    }
}
