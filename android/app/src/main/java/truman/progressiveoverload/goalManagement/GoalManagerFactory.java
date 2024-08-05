package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.GoalData;

class GoalManagerFactory<GoalFlavour> implements I_GoalManagerFactory<GoalFlavour> {
    public I_GoalManager<GoalFlavour> createGoalManager(GoalData<GoalFlavour> initialState) {
        return new GoalManager<>(initialState);
    }

}
