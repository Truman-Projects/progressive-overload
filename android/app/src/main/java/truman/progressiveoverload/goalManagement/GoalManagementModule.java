package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.I_GoalManagementModule;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;
import truman.progressiveoverload.measurement.I_TimestampedValue;

public class GoalManagementModule<TimestampedType extends I_TimestampedValue> implements I_GoalManagementModule<TimestampedType> {
    private final I_GoalRegistry<TimestampedType> goalRegistry_;

    public GoalManagementModule(I_GoalDataPersistenceSource<TimestampedType> persistenceSource) {
        GoalManagerFactory<TimestampedType> goalManagerFactory = new GoalManagerFactory<>();
        GoalRegistryFactory<TimestampedType> goalRegistryFactory = new GoalRegistryFactory<>(goalManagerFactory);
        GoalRegistryInitializer<TimestampedType> registryInitializer = new GoalRegistryInitializer<>(persistenceSource,
                goalRegistryFactory);
        goalRegistry_ = registryInitializer.initializeGoalRegistry();
    }

    @Override
    public I_GoalRegistryNotifier<TimestampedType> goalRegistryNotifier() {
        return goalRegistry_;
    }

    @Override
    public I_GoalRegistryUpdater<TimestampedType> goalRegistryUpdater() {
        return goalRegistry_;
    }
}
