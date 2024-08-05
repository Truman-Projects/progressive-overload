package truman.progressiveoverload.goalManagement.api;

public interface I_GoalNotifier<GoalFlavour> {
    void registerListener(I_GoalListener<GoalFlavour> listener);

    void unregisterListener(I_GoalListener<GoalFlavour> listener);

    GoalData<GoalFlavour> currentState();


}
