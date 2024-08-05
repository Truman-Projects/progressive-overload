package truman.progressiveoverload.goalFlavours.useCase;

import java.time.Duration;
import java.util.HashMap;
import java.util.Optional;

import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalFlavours.useCase.api.GoalFlavour;
import truman.progressiveoverload.goalFlavours.useCase.api.I_GoalIdToFlavourMap;
import truman.progressiveoverload.measurement.distance.Distance;
import truman.progressiveoverload.measurement.mass.Mass;
import truman.progressiveoverload.measurement.velocity.Velocity;

public class GoalIdToFlavourMap implements I_GoalIdToFlavourMap {
    private final HashMap<Long, GoalFlavour> goalIdToFlavourMap_;

    public GoalIdToFlavourMap(I_GoalRegistryNotifier<Mass> massGoalSource,
                              I_GoalRegistryNotifier<Distance> distanceGoalSource,
                              I_GoalRegistryNotifier<Duration> durationGoalSource,
                              I_GoalRegistryNotifier<Velocity> velocityGoalSource,
                              I_GoalRegistryNotifier<Double> customGoalSource) {
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
