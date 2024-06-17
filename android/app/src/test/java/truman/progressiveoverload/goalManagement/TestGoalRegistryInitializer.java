package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomArrayList;
import truman.progressiveoverload.randomUtilities.RandomInt;
import truman.progressiveoverload.randomUtilities.RandomOther;
import truman.progressiveoverload.randomUtilities.RandomString;

class TestGoalRegistryInitializer {

    // intermediate interfaces to appease the mockito gods
    private interface I_FakeValueGoalDataPersistenceSource extends I_GoalDataPersistenceSource<FakeTimestampedValue> {
    }

    private interface I_FakeValueGoalManagerFactory extends I_GoalManagerFactory<FakeTimestampedValue> {
    }

    private I_FakeValueGoalDataPersistenceSource persistenceSource_;
    private I_FakeValueGoalManagerFactory goalManagerFactory_;
    private GoalRegistryInitializer<FakeTimestampedValue> patient_;

    @BeforeEach
    public void resetEverything() {
        persistenceSource_ = mock(I_FakeValueGoalDataPersistenceSource.class);
        goalManagerFactory_ = mock(I_FakeValueGoalManagerFactory.class);
        patient_ = new GoalRegistryInitializer<>(persistenceSource_, goalManagerFactory_);
    }

    @Test
    public void willCreateCorrectTypeOfGoalRegistry() {
        I_GoalRegistry<FakeTimestampedValue> goalRegistry = patient_.createGoalRegistry();

        assertTrue(goalRegistry instanceof GoalRegistry);
    }

    @Test
    public void willRequestDataFromPersistenceSourceWhenCreatingRegistry() {
        patient_.createGoalRegistry();

        verify(persistenceSource_, times(1)).loadGoalDataFromMemory();
    }

    @Test
    public void willInitializeGoalRegistryWithDataFromPersistenceSource() {
        int numberOfGoalsInPersistence = new RandomInt().generate(1, 5);
        HashSet<String> uniqueGoalNames = new HashSet<>();
        ArrayList<GoalData<FakeTimestampedValue>> randomGoalDataListWithUniqueNames = new ArrayList<>();
        RandomOther<String> uniqueStringGenerator = new RandomOther<>(new RandomString());
        ArrayList<GoalData<FakeTimestampedValue>> randomGoalDataList =
                new RandomArrayList<>(new RandomGoalData()).generate(numberOfGoalsInPersistence);
        for (int index = 0; index < numberOfGoalsInPersistence; index++) {
            String goalNameForIndex = uniqueStringGenerator.otherThan(uniqueGoalNames);
            uniqueGoalNames.add(goalNameForIndex);
            randomGoalDataListWithUniqueNames.add(randomGoalDataList.get(index).withName(goalNameForIndex));
        }
        when(persistenceSource_.loadGoalDataFromMemory()).thenReturn(randomGoalDataListWithUniqueNames);

        I_GoalRegistry<FakeTimestampedValue> goalRegistry = patient_.createGoalRegistry();

        assertEquals(uniqueGoalNames, goalRegistry.currentGoalNames());
    }


}