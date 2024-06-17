package truman.progressiveoverload.goalManagement;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public class GoalRegistryFactory<TimestampedType extends I_TimestampedValue> implements I_GoalRegistryFactory<TimestampedType> {
    private final I_GoalManagerFactory<TimestampedType> goalManagerFactory_;

    public GoalRegistryFactory(I_GoalManagerFactory<TimestampedType> goalManagerFactory) {
        goalManagerFactory_ = goalManagerFactory;
    }

    @Override
    public I_GoalRegistry<TimestampedType> createGoalRegistry() {
        return new GoalRegistry<>(goalManagerFactory_);
    }
}
