package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.GoalType;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdateListener;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdateNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.measurement.I_TimestampedValue;

interface I_GoalManager<TimestampedType extends I_TimestampedValue> extends I_GoalUpdateNotifier<TimestampedType>,
        I_GoalUpdater<TimestampedType> {
    // I_GoalUpdateNotifier
    @Override
    void registerListener(I_GoalUpdateListener<TimestampedType> listener);

    @Override
    void unregisterListener(I_GoalUpdateListener<TimestampedType> listener);

    @Override
    GoalData<TimestampedType> currentState();

    // I_GoalUpdater
    @Override
    void changeGoalDescription(String newDescription);

    @Override
    void changeGoalType(GoalType newGoalType);

    @Override
    Long addRecord(TimestampedType record);

    @Override
    void removeRecord(Long recordId) throws IndexOutOfBoundsException;

    @Override
    void editRecord(Long recordId, TimestampedType updatedRecord) throws IndexOutOfBoundsException;

    @Override
    Long addTargetMilestone(TimestampedType targetMilestone);

    @Override
    void removeTargetMilestone(Long milestoneId) throws IndexOutOfBoundsException;

    @Override
    void editTargetMilestone(Long milestoneId, TimestampedType updatedMilestone) throws IndexOutOfBoundsException;

}
