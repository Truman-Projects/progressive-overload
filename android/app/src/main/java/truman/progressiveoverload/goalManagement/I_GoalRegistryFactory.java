package truman.progressiveoverload.goalManagement;

public interface I_GoalRegistryFactory<GoalFlavour> {
    I_GoalRegistry<GoalFlavour> createGoalRegistry();
}
