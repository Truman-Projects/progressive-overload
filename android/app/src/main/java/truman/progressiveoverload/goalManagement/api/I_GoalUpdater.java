package truman.progressiveoverload.goalManagement.api;

public interface I_GoalUpdater<GoalFlavour> {
    void changeGoalName(String newName);

    void changeGoalDescription(String newDescription);

    void changeGoalType(GoalType newGoalType);

    // returns recordId of record that was inserted
    Long addRecord(TimestampedValue<GoalFlavour> record);

    void removeRecord(Long recordId) throws InvalidQueryException;

    void editRecord(Long recordId, TimestampedValue<GoalFlavour> updatedRecord) throws InvalidQueryException;

    // returns milestoneId of milestone that was inserted
    Long addTargetMilestone(TimestampedValue<GoalFlavour> targetMilestone);

    void removeTargetMilestone(Long milestoneId) throws InvalidQueryException;

    void editTargetMilestone(Long milestoneId, TimestampedValue<GoalFlavour> updatedMilestone) throws InvalidQueryException;
}
