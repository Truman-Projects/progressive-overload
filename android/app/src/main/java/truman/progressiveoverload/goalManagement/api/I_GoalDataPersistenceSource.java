package truman.progressiveoverload.goalManagement.api;

import java.util.HashMap;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalDataPersistenceSource<TimestampedType extends I_TimestampedValue> {
    HashMap<Long, GoalData<TimestampedType>> loadGoalDataFromMemory();
}
