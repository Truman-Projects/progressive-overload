package truman.progressiveoverload.goalManagement.api;

public interface I_GoalListener<GoalFlavour> {
    void goalNameChanged(String newName);

    void goalDescriptionChanged(String newDescription);

    void goalTypeChanged(GoalType newGoalType);

    void recordAdded(Long recordId, TimestampedValue<GoalFlavour> record);

    void recordRemoved(Long recordId);

    void recordChanged(Long recordId, TimestampedValue<GoalFlavour> updatedRecord);

    void targetMilestoneAdded(Long milestoneId, TimestampedValue<GoalFlavour> targetMilestone);

    void targetMilestoneRemoved(Long milestoneId);

    void targetMilestoneChanged(Long recordId, TimestampedValue<GoalFlavour> updatedRecord);
}
