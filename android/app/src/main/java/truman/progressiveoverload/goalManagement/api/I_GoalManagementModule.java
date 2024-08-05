package truman.progressiveoverload.goalManagement.api;

public interface I_GoalManagementModule<GoalFlavour> {
    I_GoalRegistryNotifier<GoalFlavour> goalRegistryNotifier();

    I_GoalRegistryUpdater<GoalFlavour> goalRegistryUpdater();
}
