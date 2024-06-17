package truman.progressiveoverload.goalManagement.api;

public interface I_GoalRegistryListener {
    void goalAdded(String goalName);

    void goalRemoved(String goalName);
}
