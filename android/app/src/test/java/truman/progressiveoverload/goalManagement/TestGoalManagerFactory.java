package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.FakeTimestampedValue;

class TestGoalManagerFactory {
    @Test
    public void willCreateGoalManagerWithCorrectInitialState() {
        GoalData<FakeTimestampedValue> goalManagerInitialState = new RandomGoalData().generate();
        GoalManagerFactory<FakeTimestampedValue> patient = new GoalManagerFactory<>();

        I_GoalManager<FakeTimestampedValue> goalManager = patient.createGoalManager(goalManagerInitialState);

        assertEquals(goalManagerInitialState, goalManager.currentState());
    }

}