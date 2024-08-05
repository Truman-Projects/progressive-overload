package truman.progressiveoverload.goalManagement.api;

import java.util.HashMap;

public interface I_GoalDataPersistenceSource<GoalFlavour> {
    HashMap<Long, GoalData<GoalFlavour>> loadGoalDataFromMemory();
}
