package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.GoalData;

interface I_GoalManagerFactory<GoalFlavour> {
    I_GoalManager<GoalFlavour> createGoalManager(GoalData<GoalFlavour> initialState);
}
