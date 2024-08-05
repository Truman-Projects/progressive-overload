package truman.progressiveoverload.goalWriting.useCase.api;

import truman.progressiveoverload.goalFlavours.useCase.api.GoalFlavour;

public interface I_GoalWriter {
    Long createGoal(String goalName, String goalDescription, GoalPolarity goalPolarity, GoalFlavour goalFlavour);
}
