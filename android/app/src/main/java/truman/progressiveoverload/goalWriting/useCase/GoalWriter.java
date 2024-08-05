package truman.progressiveoverload.goalWriting.useCase;

import java.time.Duration;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.GoalType;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;
import truman.progressiveoverload.goalFlavours.useCase.api.GoalFlavour;
import truman.progressiveoverload.goalWriting.useCase.api.GoalPolarity;
import truman.progressiveoverload.goalWriting.useCase.api.I_GoalWriter;
import truman.progressiveoverload.measurement.distance.Distance;
import truman.progressiveoverload.measurement.mass.Mass;
import truman.progressiveoverload.measurement.velocity.Velocity;

class GoalWriter implements I_GoalWriter {
    private final I_GoalRegistryUpdater<Mass> massGoalUpdater_;
    private final I_GoalRegistryUpdater<Distance> distanceGoalUpdater_;
    private final I_GoalRegistryUpdater<Duration> durationGoalUpdater_;
    private final I_GoalRegistryUpdater<Velocity> velocityGoalUpdater_;
    private final I_GoalRegistryUpdater<Double> customGoalUpdater_;

    public GoalWriter(I_GoalRegistryUpdater<Mass> massGoalUpdater,
                      I_GoalRegistryUpdater<Distance> distanceGoalUpdater,
                      I_GoalRegistryUpdater<Duration> durationGoalUpdater,
                      I_GoalRegistryUpdater<Velocity> velocityGoalUpdater,
                      I_GoalRegistryUpdater<Double> customGoalUpdater) {
        massGoalUpdater_ = massGoalUpdater;
        distanceGoalUpdater_ = distanceGoalUpdater;
        durationGoalUpdater_ = durationGoalUpdater;
        velocityGoalUpdater_ = velocityGoalUpdater;
        customGoalUpdater_ = customGoalUpdater;

    }

    @Override
    public Long createGoal(String goalName, String goalDescription, GoalPolarity goalPolarity, GoalFlavour goalFlavour) {
        Long newGoalId;
        switch (goalFlavour) {
            case MASS:
                GoalData<Mass> massGoalData = new GoalData<>(goalName, goalDescription, goalPolarityToGoalType(goalPolarity));
                newGoalId = massGoalUpdater_.addGoal(massGoalData);
                break;
            case DISTANCE:
                GoalData<Distance> distanceGoalData = new GoalData<>(goalName, goalDescription,
                        goalPolarityToGoalType(goalPolarity));
                newGoalId = distanceGoalUpdater_.addGoal(distanceGoalData);
                break;
            case DURATION:
                GoalData<Duration> durationGoalData = new GoalData<>(goalName, goalDescription,
                        goalPolarityToGoalType(goalPolarity));
                newGoalId = durationGoalUpdater_.addGoal(durationGoalData);
                break;
            case VELOCITY:
                GoalData<Velocity> velocityGoalData = new GoalData<>(goalName, goalDescription,
                        goalPolarityToGoalType(goalPolarity));
                newGoalId = velocityGoalUpdater_.addGoal(velocityGoalData);
                break;
            case CUSTOM:
                GoalData<Double> customTypeGoalData = new GoalData<>(goalName, goalDescription,
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
