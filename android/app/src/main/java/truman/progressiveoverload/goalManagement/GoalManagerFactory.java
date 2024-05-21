package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.measurement.I_TimestampedValue;

class GoalManagerFactory<TimestampedType extends I_TimestampedValue> implements I_GoalManagerFactory<TimestampedType> {
    public I_GoalManager<TimestampedType> createGoalManager(GoalData<TimestampedType> initialState) {
        return new GoalManager<>(initialState);
    }

}
