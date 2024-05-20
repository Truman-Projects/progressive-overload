package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalUpdateNotifier<TimestampedType extends I_TimestampedValue> {
    void registerListener(I_GoalUpdateListener<TimestampedType> listener);

    void unregisterListener(I_GoalUpdateListener<TimestampedType> listener);

    GoalData<TimestampedType> currentState();


}
