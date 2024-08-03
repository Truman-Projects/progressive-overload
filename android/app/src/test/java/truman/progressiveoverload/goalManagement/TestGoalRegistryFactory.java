package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.fake.FakeTimestampedValue;
import truman.progressiveoverload.measurement.fake.RandomFakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestGoalRegistryFactory {
    // intermediate interface to appease the mockito gods

    private interface I_FakeValuePersistenceSource extends I_GoalDataPersistenceSource<FakeTimestampedValue> {
    }

    private interface I_FakeValueGoalManagerFactory extends I_GoalManagerFactory<FakeTimestampedValue> {
    }

    private I_FakeValuePersistenceSource persistenceSource_;
    private I_FakeValueGoalManagerFactory goalManagerFactory_;
    private UniqueIdSource idSource_;
    private GoalRegistryFactory<FakeTimestampedValue> patient_;

    @BeforeEach
    public void resetEverything() {
        persistenceSource_ = mock(I_FakeValuePersistenceSource.class);
        goalManagerFactory_ = mock(I_FakeValueGoalManagerFactory.class);
        idSource_ = new UniqueIdSource(new RandomLongGenerator());
        patient_ = new GoalRegistryFactory<>(persistenceSource_, goalManagerFactory_, idSource_);
    }

    @Test
    public void willCreateCorrectTypeOfGoalRegistry() {
        I_GoalRegistry<FakeTimestampedValue> goalRegistry = patient_.createGoalRegistry();

        assertTrue(goalRegistry instanceof GoalRegistry);
    }

    @Test
    public void willConstructGoalRegistryWithPersistenceData() {
        HashMap<Long, GoalData<FakeTimestampedValue>> persistenceData = new RandomHashMap<>(new RandomLong(),
                new RandomGoalData<>(new RandomFakeTimestampedValue())).generate();
        when(persistenceSource_.loadGoalDataFromMemory()).thenReturn(persistenceData);

        I_GoalRegistry<FakeTimestampedValue> goalRegistry = patient_.createGoalRegistry();

        assertEquals(persistenceData.keySet(), goalRegistry.currentGoalIds());
    }
}