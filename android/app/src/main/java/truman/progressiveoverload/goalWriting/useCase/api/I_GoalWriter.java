package truman.progressiveoverload.goalWriting.useCase.api;

import truman.progressiveoverload.goalUnitMapping.useCase.api.GoalUnit;

public interface I_GoalWriter {
    Long createGoal(String goalName, String goalDescription, GoalPolarity goalPolarity, GoalUnit goalUnit);
}
