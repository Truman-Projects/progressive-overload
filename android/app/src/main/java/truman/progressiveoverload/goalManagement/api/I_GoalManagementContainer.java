package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.goalManagement.GoalManagementModule;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;

public interface I_GoalManagementContainer {
    GoalManagementModule<TimestampedMass> massModule();

    GoalManagementModule<TimestampedDistance> distanceModule();

    GoalManagementModule<TimestampedDuration> durationModule();
}
