package truman.progressiveoverload.goalFlavours.useCase;

import java.util.HashMap;
import java.util.Optional;

import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalFlavours.useCase.api.GoalFlavour;
import truman.progressiveoverload.goalFlavours.useCase.api.I_GoalIdToFlavourMap;
import truman.progressiveoverload.measurement.custom.TimestampedCustomValue;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;
import truman.progressiveoverload.measurement.velocity.TimestampedVelocity;

public class GoalIdToFlavourMap implements I_GoalIdToFlavourMap {
    private final HashMap<Long, GoalFlavour> goalIdToFlavourMap_;

    public GoalIdToFlavourMap(I_GoalRegistryNotifier<TimestampedMass> massGoalSource,
                              I_GoalRegistryNotifier<TimestampedDistance> distanceGoalSource,
                              I_GoalRegistryNotifier<TimestampedDuration> durationGoalSource,
                              I_GoalRegistryNotifier<TimestampedVelocity> velocityGoalSource,
                              I_GoalRegistryNotifier<TimestampedCustomValue> customGoalSource) {
        goalIdToFlavourMap_ = new HashMap<>();
        for (Long massGoalId : massGoalSource.currentGoalIds()) {
            goalIdToFlavourMap_.put(massGoalId, GoalFlavour.MASS);
        }
        for (Long distanceGoalId : distanceGoalSource.currentGoalIds()) {
            goalIdToFlavourMap_.put(distanceGoalId, GoalFlavour.DISTANCE);
        }
        for (Long durationGoalId : durationGoalSource.currentGoalIds()) {
            goalIdToFlavourMap_.put(durationGoalId, GoalFlavour.DURATION);
        }
        for (Long velocityGoalId : velocityGoalSource.currentGoalIds()) {
            goalIdToFlavourMap_.put(velocityGoalId, GoalFlavour.VELOCITY);
        }
        for (Long customGoalId : customGoalSource.currentGoalIds()) {
            goalIdToFlavourMap_.put(customGoalId, GoalFlavour.CUSTOM);
        }
        massGoalSource.registerListener(new GoalRegistryListener(GoalFlavour.MASS));
        distanceGoalSource.registerListener(new GoalRegistryListener(GoalFlavour.DISTANCE));
        durationGoalSource.registerListener(new GoalRegistryListener(GoalFlavour.DURATION));
        velocityGoalSource.registerListener(new GoalRegistryListener(GoalFlavour.VELOCITY));
        customGoalSource.registerListener(new GoalRegistryListener(GoalFlavour.CUSTOM));
    }

    @Override
    public Optional<GoalFlavour> flavourForGoalId(Long goalId) {
        GoalFlavour flavourForGoalId = goalIdToFlavourMap_.get(goalId);

        if (flavourForGoalId == null) {
            return Optional.empty();
        } else {
            return Optional.of(flavourForGoalId);
        }
    }

    private class GoalRegistryListener implements I_GoalRegistryListener {
        private final GoalFlavour flavour_;

        public GoalRegistryListener(GoalFlavour flavour) {
            flavour_ = flavour;
        }

        @Override
        public void goalAdded(Long goalId) {
            goalIdToFlavourMap_.put(goalId, flavour_);
        }

        @Override
        public void goalRemoved(Long goalId) {
            goalIdToFlavourMap_.remove(goalId);
        }
    }
}
