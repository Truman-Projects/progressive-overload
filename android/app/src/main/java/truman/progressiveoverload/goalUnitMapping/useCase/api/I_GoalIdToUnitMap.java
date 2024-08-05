package truman.progressiveoverload.goalUnitMapping.useCase.api;

import java.util.Optional;

public interface I_GoalIdToUnitMap {
    Optional<GoalUnit> unitForGoalId(Long goalId);
}
