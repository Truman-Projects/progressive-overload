package truman.progressiveoverload.goalWriting.useCase;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.GoalType;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;
import truman.progressiveoverload.goalWriting.useCase.api.GoalUnit;
import truman.progressiveoverload.goalWriting.useCase.api.GoalPolarity;
import truman.progressiveoverload.goalWriting.useCase.api.I_GoalWriter;
import truman.progressiveoverload.measurement.custom.TimestampedCustomValue;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;
import truman.progressiveoverload.measurement.velocity.TimestampedVelocity;

class GoalWriter implements I_GoalWriter {
    private final I_GoalRegistryUpdater<TimestampedMass> massGoalUpdater_;
    private final I_GoalRegistryUpdater<TimestampedDistance> distanceGoalUpdater_;
    private final I_GoalRegistryUpdater<TimestampedDuration> durationGoalUpdater_;
    private final I_GoalRegistryUpdater<TimestampedVelocity> velocityGoalUpdater_;
    private final I_GoalRegistryUpdater<TimestampedCustomValue> customGoalUpdater_;

    public GoalWriter(I_GoalRegistryUpdater<TimestampedMass> massGoalUpdater,
                      I_GoalRegistryUpdater<TimestampedDistance> distanceGoalUpdater,
                      I_GoalRegistryUpdater<TimestampedDuration> durationGoalUpdater,
                      I_GoalRegistryUpdater<TimestampedVelocity> velocityGoalUpdater,
                      I_GoalRegistryUpdater<TimestampedCustomValue> customGoalUpdater) {
        massGoalUpdater_ = massGoalUpdater;
        distanceGoalUpdater_ = distanceGoalUpdater;
        durationGoalUpdater_ = durationGoalUpdater;
        velocityGoalUpdater_ = velocityGoalUpdater;
        customGoalUpdater_ = customGoalUpdater;

    }

    @Override
    public Long createGoal(String goalName, String goalDescription, GoalPolarity goalPolarity, GoalUnit goalUnit) {
        Long newGoalId;
        switch (goalUnit) {
            case MASS:
                GoalData<TimestampedMass> massGoalData = new GoalData<>(goalName, goalDescription, goalPolarityToGoalType(goalPolarity));
                newGoalId = massGoalUpdater_.addGoal(massGoalData);
                break;
            case DISTANCE:
                GoalData<TimestampedDistance> distanceGoalData = new GoalData<>(goalName, goalDescription,
                        goalPolarityToGoalType(goalPolarity));
                newGoalId = distanceGoalUpdater_.addGoal(distanceGoalData);
                break;
            case DURATION:
                GoalData<TimestampedDuration> durationGoalData = new GoalData<>(goalName, goalDescription,
                        goalPolarityToGoalType(goalPolarity));
                newGoalId = durationGoalUpdater_.addGoal(durationGoalData);
                break;
            case VELOCITY:
                GoalData<TimestampedVelocity> velocityGoalData = new GoalData<>(goalName, goalDescription,
                        goalPolarityToGoalType(goalPolarity));
                newGoalId = velocityGoalUpdater_.addGoal(velocityGoalData);
                break;
            case CUSTOM:
                GoalData<TimestampedCustomValue> customTypeGoalData = new GoalData<>(goalName, goalDescription,
                        goalPolarityToGoalType(goalPolarity));
                newGoalId = customGoalUpdater_.addGoal(customTypeGoalData);
                break;
            default:
                return null;

        }
        return newGoalId;
    }

    private GoalType goalPolarityToGoalType(GoalPolarity polarity) {
        switch (polarity) {
            case MINIMIZE:
                return GoalType.MINIMIZE;
            case MAXIMIZE:
                return GoalType.MAXIMIZE;
            default:
                return GoalType.MAXIMIZE;
        }
    }
}
