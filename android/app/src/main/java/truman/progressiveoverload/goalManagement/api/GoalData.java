package truman.progressiveoverload.goalManagement.api;

import java.util.HashMap;

import truman.progressiveoverload.measurement.I_TimestampedValue;

// MUST REMAIN IMMUTABLE
public final class GoalData<TimestampedType extends I_TimestampedValue> {

    private final String name_; // Should never be changed.  Each Goal's name acts as its primary key
    private final String description_;
    private final GoalType goalType_;
    private final HashMap<Long, TimestampedType> recordsById_;
    private final HashMap<Long, TimestampedType> targetMilestonesById_;

    public GoalData(String name, String description, GoalType goalType, HashMap<Long, TimestampedType> records, HashMap<Long,
            TimestampedType> targetMilestones) {
        name_ = name;
        description_ = description;
        goalType_ = goalType;
        recordsById_ = new HashMap<>(records);
        targetMilestonesById_ = new HashMap<>(targetMilestones);
    }

    public GoalData(String name, String description, GoalType goalType, HashMap<Long, TimestampedType> records) {
        this(name, description, goalType, records, new HashMap<>());
    }

    public GoalData(String name, String description, GoalType goalType) {
        this(name, description, goalType, new HashMap<>());
    }

    public String name() {
        return name_;
    }

    public String description() {
        return description_;
    }

    public GoalData<TimestampedType> withDescription(String description) {
        return new GoalData<>(name_, description, goalType_, recordsById_, targetMilestonesById_);
    }

    public GoalType goalType() {
        return goalType_;
    }

    public GoalData<TimestampedType> withGoalType(GoalType goalType) {
        return new GoalData<>(name_, description_, goalType, recordsById_, targetMilestonesById_);
    }

    public HashMap<Long, TimestampedType> recordsById() {
        return new HashMap<>(recordsById_);
    }

    public GoalData<TimestampedType> withRecordsById(HashMap<Long, TimestampedType> records) {
        return new GoalData<>(name_, description_, goalType_, records, targetMilestonesById_);
    }

    public HashMap<Long, TimestampedType> targetMilestonesById() {
        return new HashMap<>(targetMilestonesById_);
    }

    public GoalData<TimestampedType> withTargetMilestonesById(HashMap<Long, TimestampedType> targetMilestones) {
        return new GoalData<>(name_, description_, goalType_, recordsById_, targetMilestones);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GoalData)) {
            return false;
        }
        return (this.name_.equals(((GoalData<?>) other).name()) &&
                this.description_.equals(((GoalData<?>) other).description()) &&
                this.goalType_.equals(((GoalData<?>) other).goalType()) &&
                this.recordsById_.equals(((GoalData<?>) other).recordsById()) &&
                this.targetMilestonesById_.equals(((GoalData<?>) other).targetMilestonesById())
        );
    }


}
