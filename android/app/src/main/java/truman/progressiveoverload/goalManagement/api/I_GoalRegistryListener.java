package truman.progressiveoverload.goalManagement.api;

public interface I_GoalRegistryListener {
    void goalAdded(Long goalId);

    void goalRemoved(Long goalId);
}
