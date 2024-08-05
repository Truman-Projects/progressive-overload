package truman.progressiveoverload.goalManagement.api;

import java.util.HashSet;

public interface I_GoalRegistryNotifier<GoalFlavour> {
    void registerListener(I_GoalRegistryListener listener);

    void unregisterListener(I_GoalRegistryListener listener);

    HashSet<Long> currentGoalIds();

    I_GoalNotifier<GoalFlavour> goalUpdateNotifierByGoalId(Long goalId) throws InvalidQueryException;
}
