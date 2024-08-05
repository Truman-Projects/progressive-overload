package truman.progressiveoverload.goalFlavours.useCase.api;

import java.util.Optional;

public interface I_GoalIdToFlavourMap {
    Optional<GoalFlavour> flavourForGoalId(Long goalId);
}
