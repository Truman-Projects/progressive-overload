package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalNotifier<TimestampedType extends I_TimestampedValue> {
    void registerListener(I_GoalListener<TimestampedType> listener);

    void unregisterListener(I_GoalListener<TimestampedType> listener);

    GoalData<TimestampedType> currentState();


}
