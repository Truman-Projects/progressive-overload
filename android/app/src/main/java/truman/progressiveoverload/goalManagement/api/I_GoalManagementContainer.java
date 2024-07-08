package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.goalManagement.GoalManagementModule;
import truman.progressiveoverload.measurement.custom.TimestampedCustomValue;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;
import truman.progressiveoverload.measurement.velocity.TimestampedVelocity;

public interface I_GoalManagementContainer {
    GoalManagementModule<TimestampedMass> massModule();

    GoalManagementModule<TimestampedDistance> distanceModule();

    GoalManagementModule<TimestampedDuration> durationModule();

    GoalManagementModule<TimestampedVelocity> velocityModule();

    GoalManagementModule<TimestampedCustomValue> customUnitModule();
}
