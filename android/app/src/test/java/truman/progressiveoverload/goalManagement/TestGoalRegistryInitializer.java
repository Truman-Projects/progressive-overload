package truman.progressiveoverload.goalManagement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestGoalRegistryInitializer {

    // intermediate interfaces to appease the mockito gods
    private interface I_FakeValueGoalDataPersistenceSource extends I_GoalDataPersistenceSource<FakeTimestampedValue> {
    }

    private interface I_FakeValueGoalRegistryFactory extends I_GoalRegistryFactory<FakeTimestampedValue> {
    }

    private interface I_FakeValueGoalRegistry extends I_GoalRegistry<FakeTimestampedValue> {
    }

    private I_GoalDataPersistenceSource<FakeTimestampedValue> persistenceSource_;
    private I_GoalRegistry<FakeTimestampedValue> goalRegistry_;
    private I_GoalRegistryFactory<FakeTimestampedValue> goalRegistryFactory_;
    private GoalRegistryInitializer<FakeTimestampedValue> patient_;

    @BeforeEach
    public void resetEverything() {
        goalRegistry_ = mock(I_FakeValueGoalRegistry.class);
        persistenceSource_ = mock(I_FakeValueGoalDataPersistenceSource.class);
        goalRegistryFactory_ = mock(I_FakeValueGoalRegistryFactory.class);
        when(goalRegistryFactory_.createGoalRegistry()).thenReturn(goalRegistry_);
        patient_ = new GoalRegistryInitializer<>(persistenceSource_, goalRegistryFactory_);
    }

    @Test
    public void willRequestRegistryFromFactoryWhenInitializing() {
        patient_.initializeGoalRegistry();

        verify(goalRegistryFactory_, times(1)).createGoalRegistry();
    }

    @Test
    public void willRequestDataFromPersistenceSourceWhenInitialize() {
        patient_.initializeGoalRegistry();

        verify(persistenceSource_, times(1)).loadGoalDataFromMemory();
    }

    @Test
    public void willInitializeGoalRegistryWithPersistenceData() {
        HashMap<Long, GoalData<FakeTimestampedValue>> persistenceData =
                new RandomHashMap<>(new RandomLong(), new RandomGoalData()).generate();
        when(persistenceSource_.loadGoalDataFromMemory()).thenReturn(persistenceData);

        patient_.initializeGoalRegistry();

        verify(goalRegistry_, times(1)).initializeWithExistingGoals(persistenceData);
    }


}