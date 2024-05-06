package truman.progressiveoverload.goalManagement.api;

import java.util.ArrayList;

import truman.progressiveoverload.measurement.I_TimestampedValue;

// MUST REMAIN IMMUTABLE
public final class GoalData<TimestampedType extends I_TimestampedValue> {

    private final String name_;
    private final String description_;
    private final GoalType goalType_;
    private final ArrayList<TimestampedType> records_;
    private final ArrayList<TimestampedType> targetMilestones_;

    public GoalData(String name, String description, GoalType goalType,
                    ArrayList<TimestampedType> records,
                    ArrayList<TimestampedType> targetMilestones) {
        name_ = name;
        description_ = description;
        goalType_ = goalType;
        records_ = new ArrayList<>(records);
        targetMilestones_ = new ArrayList<>(targetMilestones);
    }

    public GoalData(String name, String description, GoalType goalType,
                    ArrayList<TimestampedType> records) {
        this(name, description, goalType, records, new ArrayList<>());
    }

    public GoalData(String name, String description, GoalType goalType) {
        this(name, description, goalType, new ArrayList<>());
    }

    public String name() {
        return name_;
    }

    public String description() {
        return description_;
    }

    public GoalData<TimestampedType> withDescription(String description) {
        return new GoalData<>(name_, description, goalType_, records_, targetMilestones_);
    }

    public GoalType goalType() {
        return goalType_;
    }

    public GoalData<TimestampedType> withGoalType(GoalType goalType) {
        return new GoalData<>(name_, description_, goalType, records_, targetMilestones_);
    }

    public ArrayList<TimestampedType> records() {
        return new ArrayList<>(records_);
    }

    public GoalData<TimestampedType> withRecords(ArrayList<TimestampedType> records) {
        return new GoalData<>(name_, description_, goalType_, records, targetMilestones_);
    }

    public ArrayList<TimestampedType> targetMilestones() {
        return new ArrayList<>(targetMilestones_);
    }

    public GoalData<TimestampedType> withTargetMilestones(ArrayList<TimestampedType> targetMilestones) {
        return new GoalData<>(name_, description_, goalType_, records_, targetMilestones);
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
                this.records_.equals(((GoalData<?>) other).records()) &&
                this.targetMilestones_.equals(((GoalData<?>) other).targetMilestones())
        );
    }


}
