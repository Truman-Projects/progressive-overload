package truman.progressiveoverload.goalManagement;

import java.util.HashMap;

import truman.progressiveoverload.measurement.I_TimestampedValue;

public class GoalRegistryFactory<TimestampedType extends I_TimestampedValue> implements I_GoalRegistryFactory<TimestampedType> {
    private final I_GoalManagerFactory<TimestampedType> goalManagerFactory_;
    private final UniqueIdSource idSource_;

    public GoalRegistryFactory(I_GoalManagerFactory<TimestampedType> goalManagerFactory) {
        goalManagerFactory_ = goalManagerFactory;
        // TODO: wire in id source properly
        I_RandomLongGenerator rng = new RandomLongGenerator();
        idSource_ = new UniqueIdSource(rng);
    }

    @Override
    public I_GoalRegistry<TimestampedType> createGoalRegistry() {
        return new GoalRegistry<>(new HashMap<>(), goalManagerFactory_, idSource_);// TODO: pass in proper hashmap from persistence
    }
}
