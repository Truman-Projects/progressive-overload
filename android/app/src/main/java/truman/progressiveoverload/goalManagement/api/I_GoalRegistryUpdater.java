package truman.progressiveoverload.goalManagement.api;

public interface I_GoalRegistryUpdater<GoalFlavour> {
    I_GoalUpdater<GoalFlavour> goalUpdaterByGoalId(Long goalId) throws InvalidQueryException;

    Long addGoal(GoalData<GoalFlavour> goalData);

    void removeGoal(Long goalId) throws InvalidQueryException;
}
