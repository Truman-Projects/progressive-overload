package truman.progressiveoverload.goalWriting.useCase.api;

public interface I_GoalWriter {
    Long createGoal(String goalName, String goalDescription, GoalPolarity goalPolarity, GoalUnit goalUnit);
}
