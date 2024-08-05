package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.GoalType;
import truman.progressiveoverload.goalManagement.api.I_GoalListener;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.goalManagement.api.TimestampedValue;

interface I_GoalManager<GoalFlavour> extends I_GoalNotifier<GoalFlavour>,
        I_GoalUpdater<GoalFlavour> {
    // I_GoalUpdateNotifier
    @Override
    void registerListener(I_GoalListener<GoalFlavour> listener);

    @Override
    void unregisterListener(I_GoalListener<GoalFlavour> listener);

    @Override
    GoalData<GoalFlavour> currentState();

    // I_GoalUpdater
    @Override
    void changeGoalName(String newName);

    @Override
    void changeGoalDescription(String newDescription);

    @Override
    void changeGoalType(GoalType newGoalType);

    @Override
    Long addRecord(TimestampedValue<GoalFlavour> record);

    @Override
    void removeRecord(Long recordId) throws InvalidQueryException;

    @Override
    void editRecord(Long recordId, TimestampedValue<GoalFlavour> updatedRecord) throws InvalidQueryException;

    @Override
    Long addTargetMilestone(TimestampedValue<GoalFlavour> targetMilestone);

    @Override
    void removeTargetMilestone(Long milestoneId) throws InvalidQueryException;

    @Override
    void editTargetMilestone(Long milestoneId, TimestampedValue<GoalFlavour> updatedMilestone) throws InvalidQueryException;

}
