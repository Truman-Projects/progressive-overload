package truman.progressiveoverload.goalManagement.api;

import java.util.ArrayList;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalDataPersistenceSource<TimestampedType extends I_TimestampedValue> {
    ArrayList<GoalData<TimestampedType>> loadGoalDataFromMemory();
}
