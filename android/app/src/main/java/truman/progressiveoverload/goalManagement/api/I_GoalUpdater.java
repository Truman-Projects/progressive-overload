package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalUpdater<TimestampedType extends I_TimestampedValue> {
    void changeGoalDescription(String newDescription);

    void changeGoalType(GoalType newGoalType);

    void addRecord(Long recordId, TimestampedType record);

    void removeRecord(Long recordId, TimestampedType record);

    void editRecord(Long recordId, TimestampedType record);

    void addTargetMilestone(Long milestoneId, TimestampedType targetMilestone);

    void removeTargetMilestone(Long milestoneId, TimestampedType targetMilestone);
}
