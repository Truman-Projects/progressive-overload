package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalUpdater<TimestampedType extends I_TimestampedValue> {
    void changeGoalDescription(String newDescription);

    void changeGoalType(GoalType newGoalType);

    // returns recordId of record that was inserted
    Long addRecord(TimestampedType record);

    void removeRecord(Long recordId) throws IndexOutOfBoundsException;

    void editRecord(Long recordId, TimestampedType updatedRecord) throws IndexOutOfBoundsException;

    // returns milestoneId of milestone that was inserted
    Long addTargetMilestone(TimestampedType targetMilestone);

    void removeTargetMilestone(Long milestoneId) throws IndexOutOfBoundsException;

    void editTargetMilestone(Long milestoneId, TimestampedType updatedMilestone) throws IndexOutOfBoundsException;
}
