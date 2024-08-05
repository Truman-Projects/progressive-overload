package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestGoalManagerFactory {
    private final RandomGoalData<Long> goalDataGenerator_ = new RandomGoalData<>(new RandomLong());

    @Test
    public void willCreateCorrectTypeOfGoalManager() {
        GoalManagerFactory<Long> patient = new GoalManagerFactory<>();

        I_GoalManager<Long> goalManager = patient.createGoalManager(goalDataGenerator_.generate());

        assertTrue(goalManager instanceof GoalManager);

    }

    @Test
    public void willCreateGoalManagerWithCorrectInitialState() {
        GoalData<Long> goalManagerInitialState = goalDataGenerator_.generate();
        GoalManagerFactory<Long> patient = new GoalManagerFactory<>();

        I_GoalManager<Long> goalManager = patient.createGoalManager(goalManagerInitialState);

        assertEquals(goalManagerInitialState, goalManager.currentState());
    }

}