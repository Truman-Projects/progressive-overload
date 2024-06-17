package truman.progressiveoverload.goalManagement.api;

import truman.progressiveoverload.goalManagement.GoalManagementModule;
import truman.progressiveoverload.measurement.TimestampedMass;

public interface I_GoalManagementContainer {
    GoalManagementModule<TimestampedMass> massModule();
}
