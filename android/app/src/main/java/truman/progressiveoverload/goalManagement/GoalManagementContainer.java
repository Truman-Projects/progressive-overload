package truman.progressiveoverload.goalManagement;


import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.I_GoalManagementContainer;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.mass.TimestampedMass;

public class GoalManagementContainer implements I_GoalManagementContainer {
    private final GoalManagementModule<TimestampedMass> massModule_;
    private final GoalManagementModule<TimestampedDistance> distanceModule_;

    public GoalManagementContainer(I_GoalDataPersistenceSource<TimestampedMass> persistenceSourceForMassGoals,
                                   I_GoalDataPersistenceSource<TimestampedDistance> persistenceSourceForDistanceGoals) {
        massModule_ = new GoalManagementModule<>(persistenceSourceForMassGoals);
        distanceModule_ = new GoalManagementModule<>(persistenceSourceForDistanceGoals);
    }

    @Override
    public GoalManagementModule<TimestampedMass> massModule() {
        return massModule_;
    }

    @Override
    public GoalManagementModule<TimestampedDistance> distanceModule() {
        return distanceModule_;
    }

}
