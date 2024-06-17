package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.GoalType;
import truman.progressiveoverload.goalManagement.api.I_GoalListener;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.measurement.I_TimestampedValue;

interface I_GoalManager<TimestampedType extends I_TimestampedValue> extends I_GoalNotifier<TimestampedType>,
        I_GoalUpdater<TimestampedType> {
    // I_GoalUpdateNotifier
    @Override
    void registerListener(I_GoalListener<TimestampedType> listener);

    @Override
    void unregisterListener(I_GoalListener<TimestampedType> listener);

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
    void removeRecord(Long recordId) throws InvalidQueryException;

    @Override
    void editRecord(Long recordId, TimestampedType updatedRecord) throws InvalidQueryException;

    @Override
    Long addTargetMilestone(TimestampedType targetMilestone);

    @Override
    void removeTargetMilestone(Long milestoneId) throws InvalidQueryException;

    @Override
    void editTargetMilestone(Long milestoneId, TimestampedType updatedMilestone) throws InvalidQueryException;

}
