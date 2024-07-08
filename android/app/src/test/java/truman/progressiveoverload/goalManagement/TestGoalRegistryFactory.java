package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import truman.progressiveoverload.measurement.fake.FakeTimestampedValue;

class TestGoalRegistryFactory {
    // intermediate interface to appease the mockito gods


    private interface I_FakeValueGoalManagerFactory extends I_GoalManagerFactory<FakeTimestampedValue> {
    }

    private I_FakeValueGoalManagerFactory goalManagerFactory_;
    private GoalRegistryFactory<FakeTimestampedValue> patient_;

    @BeforeEach
    public void resetEverything() {
        goalManagerFactory_ = mock(I_FakeValueGoalManagerFactory.class);
        patient_ = new GoalRegistryFactory<>(goalManagerFactory_);
    }

    @Test
    public void willCreateCorrectTypeOfGoalRegistry() {
        I_GoalRegistry<FakeTimestampedValue> goalRegistry = patient_.createGoalRegistry();

        assertTrue(goalRegistry instanceof GoalRegistry);
    }
}