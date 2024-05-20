package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public interface I_GoalUpdateListener<TimestampedType extends I_TimestampedValue> {
    void goalDescriptionChanged(String newDescription);

    void goalTypeChanged(GoalType newGoalType);

    void recordAdded(Long recordId, TimestampedType record);

    void recordRemoved(Long recordId, TimestampedType record);

    void recordChanged(Long recordId, TimestampedType record);

    void targetMilestoneAdded(Long milestoneId, TimestampedType targetMilestone);

    void targetMilestoneRemoved(Long milestoneId, TimestampedType targetMilestone);
}
