package truman.progressiveoverload.goalManagement.api;

import java.time.Duration;

import truman.progressiveoverload.goalManagement.GoalManagementModule;
import truman.progressiveoverload.measurement.distance.Distance;
import truman.progressiveoverload.measurement.mass.Mass;
import truman.progressiveoverload.measurement.velocity.Velocity;

public interface I_GoalManagementContainer {
    GoalManagementModule<Mass> massModule();

    GoalManagementModule<Distance> distanceModule();

    GoalManagementModule<Duration> durationModule();

    GoalManagementModule<Velocity> velocityModule();

    GoalManagementModule<Double> customUnitModule();
}
