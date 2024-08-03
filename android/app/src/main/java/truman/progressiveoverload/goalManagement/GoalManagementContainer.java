package truman.progressiveoverload.goalManagement;


import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.I_GoalManagementContainer;
import truman.progressiveoverload.measurement.custom.TimestampedCustomValue;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;
import truman.progressiveoverload.measurement.velocity.TimestampedVelocity;

public class GoalManagementContainer implements I_GoalManagementContainer {
    private final UniqueIdSource idSource_;
    private final GoalManagementModule<TimestampedMass> massModule_;
    private final GoalManagementModule<TimestampedDistance> distanceModule_;
    private final GoalManagementModule<TimestampedDuration> durationModule_;
    private final GoalManagementModule<TimestampedVelocity> velocityModule_;
    private final GoalManagementModule<TimestampedCustomValue> customUnitModule_;

    public GoalManagementContainer(I_GoalDataPersistenceSource<TimestampedMass> persistenceSourceForMassGoals,
                                   I_GoalDataPersistenceSource<TimestampedDistance> persistenceSourceForDistanceGoals,
                                   I_GoalDataPersistenceSource<TimestampedDuration> persistenceSourceForDurationGoals,
                                   I_GoalDataPersistenceSource<TimestampedVelocity> persistenceSourceForVelocityGoals,
                                   I_GoalDataPersistenceSource<TimestampedCustomValue> persistenceSourceForCustomGoals) {
        idSource_ = new UniqueIdSource(new RandomLongGenerator());
        massModule_ = new GoalManagementModule<>(persistenceSourceForMassGoals, idSource_);
        distanceModule_ = new GoalManagementModule<>(persistenceSourceForDistanceGoals, idSource_);
        durationModule_ = new GoalManagementModule<>(persistenceSourceForDurationGoals, idSource_);
        velocityModule_ = new GoalManagementModule<>(persistenceSourceForVelocityGoals, idSource_);
        customUnitModule_ = new GoalManagementModule<>(persistenceSourceForCustomGoals, idSource_);
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

    @Override
    public GoalManagementModule<TimestampedVelocity> velocityModule() {
        return velocityModule_;
    }

    @Override
    public GoalManagementModule<TimestampedCustomValue> customUnitModule() {
        return customUnitModule_;
    }

}
