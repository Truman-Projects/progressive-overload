package truman.progressiveoverload.goalUnitMapping.useCase;

import java.util.HashMap;
import java.util.Optional;

import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalUnitMapping.useCase.api.GoalUnit;
import truman.progressiveoverload.goalUnitMapping.useCase.api.I_GoalIdToUnitMap;
import truman.progressiveoverload.measurement.custom.TimestampedCustomValue;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;
import truman.progressiveoverload.measurement.velocity.TimestampedVelocity;

public class GoalIdToUnitMap implements I_GoalIdToUnitMap {
    private final HashMap<Long, GoalUnit> goalIdToUnitMap_;

    public GoalIdToUnitMap(I_GoalRegistryNotifier<TimestampedMass> massGoalSource,
                           I_GoalRegistryNotifier<TimestampedDistance> distanceGoalSource,
                           I_GoalRegistryNotifier<TimestampedDuration> durationGoalSource,
                           I_GoalRegistryNotifier<TimestampedVelocity> velocityGoalSource,
                           I_GoalRegistryNotifier<TimestampedCustomValue> customGoalSource) {
        goalIdToUnitMap_ = new HashMap<>();
        for (Long massGoalId : massGoalSource.currentGoalIds()) {
            goalIdToUnitMap_.put(massGoalId, GoalUnit.MASS);
        }
        for (Long distanceGoalId : distanceGoalSource.currentGoalIds()) {
            goalIdToUnitMap_.put(distanceGoalId, GoalUnit.DISTANCE);
        }
        for (Long durationGoalId : durationGoalSource.currentGoalIds()) {
            goalIdToUnitMap_.put(durationGoalId, GoalUnit.DURATION);
        }
        for (Long velocityGoalId : velocityGoalSource.currentGoalIds()) {
            goalIdToUnitMap_.put(velocityGoalId, GoalUnit.VELOCITY);
        }
        for (Long customGoalId : customGoalSource.currentGoalIds()) {
            goalIdToUnitMap_.put(customGoalId, GoalUnit.CUSTOM);
        }
        massGoalSource.registerListener(new GoalRegistryListener(GoalUnit.MASS));
        distanceGoalSource.registerListener(new GoalRegistryListener(GoalUnit.DISTANCE));
        durationGoalSource.registerListener(new GoalRegistryListener(GoalUnit.DURATION));
        velocityGoalSource.registerListener(new GoalRegistryListener(GoalUnit.VELOCITY));
        customGoalSource.registerListener(new GoalRegistryListener(GoalUnit.CUSTOM));
    }

    @Override
    public Optional<GoalUnit> unitForGoalId(Long goalId) {
        GoalUnit unitForGoalId = goalIdToUnitMap_.get(goalId);
        
        if (unitForGoalId == null) {
            return Optional.empty();
        } else {
            return Optional.of(unitForGoalId);
        }
    }

    private class GoalRegistryListener implements I_GoalRegistryListener {
        private final GoalUnit unit_;

        public GoalRegistryListener(GoalUnit unit) {
            unit_ = unit;
        }

        @Override
        public void goalAdded(Long goalId) {
            goalIdToUnitMap_.put(goalId, unit_);
        }

        @Override
        public void goalRemoved(Long goalId) {
            goalIdToUnitMap_.remove(goalId);
        }
    }
}
