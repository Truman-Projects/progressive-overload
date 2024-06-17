package truman.progressiveoverload.goalManagement;


import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.I_GoalManagementContainer;
import truman.progressiveoverload.measurement.TimestampedMass;

public class GoalManagementContainer implements I_GoalManagementContainer {
    private final GoalManagementModule<TimestampedMass> massModule_;

    public GoalManagementContainer(I_GoalDataPersistenceSource<TimestampedMass> persistenceSourceForMassGoals) {
        massModule_ = new GoalManagementModule<>(persistenceSourceForMassGoals);
    }

    @Override
    public GoalManagementModule<TimestampedMass> massModule() {
        return massModule_;
    }
}
