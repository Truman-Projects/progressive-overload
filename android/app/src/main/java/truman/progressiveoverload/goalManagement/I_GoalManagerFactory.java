package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.measurement.I_TimestampedValue;

interface I_GoalManagerFactory<TimestampedType extends I_TimestampedValue> {
    I_GoalManager<TimestampedType> createGoalManager(GoalData<TimestampedType> initialState);
}
