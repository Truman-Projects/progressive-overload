package truman.progressiveoverload.goalManagement;


import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.I_GoalManagementContainer;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;

public class GoalManagementContainer implements I_GoalManagementContainer {
    private final GoalManagementModule<TimestampedMass> massModule_;
    private final GoalManagementModule<TimestampedDistance> distanceModule_;
    private final GoalManagementModule<TimestampedDuration> durationModule_;

    public GoalManagementContainer(I_GoalDataPersistenceSource<TimestampedMass> persistenceSourceForMassGoals,
                                   I_GoalDataPersistenceSource<TimestampedDistance> persistenceSourceForDistanceGoals,
                                   I_GoalDataPersistenceSource<TimestampedDuration> persistenceSourceForDurationGoals) {
        massModule_ = new GoalManagementModule<>(persistenceSourceForMassGoals);
        distanceModule_ = new GoalManagementModule<>(persistenceSourceForDistanceGoals);
        durationModule_ = new GoalManagementModule<>(persistenceSourceForDurationGoals);
    }

    @Override
    public GoalManagementModule<TimestampedMass> massModule() {
        return massModule_;
    }

    @Override
    public GoalManagementModule<TimestampedDistance> distanceModule() {
        return distanceModule_;
    }

    @Override
    public GoalManagementModule<TimestampedDuration> durationModule() {
        return durationModule_;
    }

}
