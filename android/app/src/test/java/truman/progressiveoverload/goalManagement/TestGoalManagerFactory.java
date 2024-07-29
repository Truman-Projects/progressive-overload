package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.fake.FakeTimestampedValue;
import truman.progressiveoverload.measurement.fake.RandomFakeTimestampedValue;

class TestGoalManagerFactory {
    private final RandomGoalData<FakeTimestampedValue> goalDataGenerator_ = new RandomGoalData<>(new RandomFakeTimestampedValue());

    @Test
    public void willCreateCorrectTypeOfGoalManager() {
        GoalManagerFactory<FakeTimestampedValue> patient = new GoalManagerFactory<>();

        I_GoalManager<FakeTimestampedValue> goalManager = patient.createGoalManager(goalDataGenerator_.generate());

        assertTrue(goalManager instanceof GoalManager);

    }

    @Test
    public void willCreateGoalManagerWithCorrectInitialState() {
        GoalData<FakeTimestampedValue> goalManagerInitialState = goalDataGenerator_.generate();
        GoalManagerFactory<FakeTimestampedValue> patient = new GoalManagerFactory<>();

        I_GoalManager<FakeTimestampedValue> goalManager = patient.createGoalManager(goalManagerInitialState);

        assertEquals(goalManagerInitialState, goalManager.currentState());
    }

}