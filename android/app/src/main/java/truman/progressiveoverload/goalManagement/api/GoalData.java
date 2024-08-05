package truman.progressiveoverload.goalManagement.api;

import java.util.HashMap;

// MUST REMAIN IMMUTABLE
public final class GoalData<GoalFlavour> {

    private final String name_; // Should never be changed.  Each Goal's name acts as its primary key
    private final String description_;
    private final GoalType goalType_;
    private final HashMap<Long, TimestampedValue<GoalFlavour>> recordsById_;
    private final HashMap<Long, TimestampedValue<GoalFlavour>> targetMilestonesById_;

    public GoalData(String name, String description, GoalType goalType, HashMap<Long, TimestampedValue<GoalFlavour>> records, HashMap<Long,
            TimestampedValue<GoalFlavour>> targetMilestones) {
        name_ = name;
        description_ = description;
        goalType_ = goalType;
        recordsById_ = new HashMap<>(records);
        targetMilestonesById_ = new HashMap<>(targetMilestones);
    }

    public GoalData(String name, String description, GoalType goalType, HashMap<Long, TimestampedValue<GoalFlavour>> records) {
        this(name, description, goalType, records, new HashMap<>());
    }

    public GoalData(String name, String description, GoalType goalType) {
        this(name, description, goalType, new HashMap<>());
    }

    public String name() {
        return name_;
    }

    public GoalData<GoalFlavour> withName(String name) {
        return new GoalData<>(name, description_, goalType_, recordsById_, targetMilestonesById_);
    }

    public String description() {
        return description_;
    }

    public GoalData<GoalFlavour> withDescription(String description) {
        return new GoalData<>(name_, description, goalType_, recordsById_, targetMilestonesById_);
    }

    public GoalType goalType() {
        return goalType_;
    }

    public GoalData<GoalFlavour> withGoalType(GoalType goalType) {
        return new GoalData<>(name_, description_, goalType, recordsById_, targetMilestonesById_);
    }

    public HashMap<Long, TimestampedValue<GoalFlavour>> recordsById() {
        return new HashMap<>(recordsById_);
    }

    public GoalData<GoalFlavour> withRecordsById(HashMap<Long, TimestampedValue<GoalFlavour>> records) {
        return new GoalData<>(name_, description_, goalType_, records, targetMilestonesById_);
    }

    public HashMap<Long, TimestampedValue<GoalFlavour>> targetMilestonesById() {
        return new HashMap<>(targetMilestonesById_);
    }

    public GoalData<GoalFlavour> withTargetMilestonesById(HashMap<Long, TimestampedValue<GoalFlavour>> targetMilestones) {
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
