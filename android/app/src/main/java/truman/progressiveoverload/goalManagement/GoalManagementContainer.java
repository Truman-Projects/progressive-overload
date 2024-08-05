package truman.progressiveoverload.goalManagement;


import java.time.Duration;

import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.I_GoalManagementContainer;
import truman.progressiveoverload.measurement.distance.Distance;
import truman.progressiveoverload.measurement.mass.Mass;
import truman.progressiveoverload.measurement.velocity.Velocity;

public class GoalManagementContainer implements I_GoalManagementContainer {
    private final UniqueIdSource idSource_;
    private final GoalManagementModule<Mass> massModule_;
    private final GoalManagementModule<Distance> distanceModule_;
    private final GoalManagementModule<Duration> durationModule_;
    private final GoalManagementModule<Velocity> velocityModule_;
    private final GoalManagementModule<Double> customUnitModule_;

    public GoalManagementContainer(I_GoalDataPersistenceSource<Mass> persistenceSourceForMassGoals,
                                   I_GoalDataPersistenceSource<Distance> persistenceSourceForDistanceGoals,
                                   I_GoalDataPersistenceSource<Duration> persistenceSourceForDurationGoals,
                                   I_GoalDataPersistenceSource<Velocity> persistenceSourceForVelocityGoals,
                                   I_GoalDataPersistenceSource<Double> persistenceSourceForCustomGoals) {
        idSource_ = new UniqueIdSource(new RandomLongGenerator());
        massModule_ = new GoalManagementModule<>(persistenceSourceForMassGoals, idSource_);
        distanceModule_ = new GoalManagementModule<>(persistenceSourceForDistanceGoals, idSource_);
        durationModule_ = new GoalManagementModule<>(persistenceSourceForDurationGoals, idSource_);
        velocityModule_ = new GoalManagementModule<>(persistenceSourceForVelocityGoals, idSource_);
        customUnitModule_ = new GoalManagementModule<>(persistenceSourceForCustomGoals, idSource_);
    }

    @Override
    public GoalManagementModule<Mass> massModule() {
        return massModule_;
    }

    @Override
    public GoalManagementModule<Distance> distanceModule() {
        return distanceModule_;
    }

    @Override
    public GoalManagementModule<Duration> durationModule() {
        return durationModule_;
    }

    @Override
    public GoalManagementModule<Velocity> velocityModule() {
        return velocityModule_;
    }

    @Override
    public GoalManagementModule<Double> customUnitModule() {
        return customUnitModule_;
    }

}
