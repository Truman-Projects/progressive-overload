package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalUpdater<TimestampedType extends I_TimestampedValue> {
    void changeGoalName(String newName);

    void changeGoalDescription(String newDescription);

    void changeGoalType(GoalType newGoalType);

    // returns recordId of record that was inserted
    Long addRecord(TimestampedType record);

    void removeRecord(Long recordId) throws InvalidQueryException;

    void editRecord(Long recordId, TimestampedType updatedRecord) throws InvalidQueryException;

    // returns milestoneId of milestone that was inserted
    Long addTargetMilestone(TimestampedType targetMilestone);

    void removeTargetMilestone(Long milestoneId) throws InvalidQueryException;

    void editTargetMilestone(Long milestoneId, TimestampedType updatedMilestone) throws InvalidQueryException;
}
