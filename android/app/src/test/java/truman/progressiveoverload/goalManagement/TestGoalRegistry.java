package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.fake.FakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomArrayList;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomInt;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomOther;

class TestGoalRegistry {
    // intermediate interfaces to appease the mockito gods

    private interface I_FakeValueGoalManagerFactory extends I_GoalManagerFactory<FakeTimestampedValue> {
    }

    private interface I_FakeValueGoalManager extends I_GoalManager<FakeTimestampedValue> {
    }


    private final RandomGoalData randomGoalDataGenerator_ = new RandomGoalData();
    private I_GoalRegistryListener[] listenerList_;
    private I_FakeValueGoalManagerFactory goalManagerFactory_;
    private GoalRegistry<FakeTimestampedValue> patient_;

    @BeforeEach
    public void resetEverything() {
        listenerList_ = new I_GoalRegistryListener[]{
                mock(I_GoalRegistryListener.class),
                mock(I_GoalRegistryListener.class)};
        goalManagerFactory_ = mock(I_FakeValueGoalManagerFactory.class);
        patient_ = new GoalRegistry<>(goalManagerFactory_);
    }

    @Test
    public void willReturnIdsOfAllAddedGoals() {
        int numberOfGoals = new RandomInt().generate(1, 5);
        ArrayList<GoalData<FakeTimestampedValue>> randomGoalDataList =
                new RandomArrayList<>(randomGoalDataGenerator_).generate(numberOfGoals);
        HashSet<Long> expectedGoalDataIDs = new HashSet<>();
        for (GoalData<FakeTimestampedValue> goal : randomGoalDataList) {
            prepareGoalManagerFactoryToCreateGoal(goal);
            Long goalId = patient_.addGoal(goal);
            expectedGoalDataIDs.add(goalId);
        }

        HashSet<Long> queriedGoalDataIDs = patient_.currentGoalIds();

        assertEquals(expectedGoalDataIDs, queriedGoalDataIDs);
    }

    @Test
    public void willNotReturnIDsOfRemovedGoals() {
        final int NUMBER_OF_GOALS = 5;
        ArrayList<GoalData<FakeTimestampedValue>> randomGoalDataList =
                new RandomArrayList<>(randomGoalDataGenerator_).generate(NUMBER_OF_GOALS);
        HashSet<Long> goalIDs = new HashSet<>();
        for (GoalData<FakeTimestampedValue> goal : randomGoalDataList) {
            prepareGoalManagerFactoryToCreateGoal(goal);
            Long goalID = patient_.addGoal(goal);
            goalIDs.add(goalID);
        }
        Long[] goalIDsAsList = goalIDs.toArray(new Long[NUMBER_OF_GOALS]);
        int indexInGoalIDListToRemove = new RandomInt().generate(0, NUMBER_OF_GOALS);
        Long goalIDToRemove = goalIDsAsList[indexInGoalIDListToRemove];
        failOnException(() -> patient_.removeGoal(goalIDToRemove));
        goalIDs.remove(goalIDToRemove);

        HashSet<Long> queriedGoalIDs = patient_.currentGoalIds();

        assertEquals(goalIDs, queriedGoalIDs);
    }

    @Test
    public void willThrowExceptionWhenRemovingNonExistentGoalId() {
        injectRandomGoalsIntoPatient();
        Long nonExistentGoalId = new RandomOther<>(new RandomLong()).otherThan(patient_.currentGoalIds());

        assertThrows(InvalidQueryException.class, () -> patient_.removeGoal(nonExistentGoalId));

    }

    @Test
    public void willCallCreateGoalManagerWhenAddingGoal() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);

        patient_.addGoal(randomGoalData);

        verify(goalManagerFactory_, times(1)).createGoalManager(randomGoalData);
    }

    @Test
    public void willNotReuseRemovedGoalIds() {
        GoalData<FakeTimestampedValue> randomGoal1 = randomGoalDataGenerator_.generate();
        GoalData<FakeTimestampedValue> randomGoal2 = randomGoalDataGenerator_.generate();
        Long firstGoalId = patient_.addGoal(randomGoal1);
        failOnException(() -> patient_.removeGoal(firstGoalId));

        Long secondGoalId = patient_.addGoal(randomGoal2);

        assertNotEquals(firstGoalId, secondGoalId);
    }


    @Test
    public void willReturnCorrectGoalUpdateNotifier() {
        injectRandomGoalsIntoPatient();
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        I_GoalManager<FakeTimestampedValue> mockGoalManager = mock(I_FakeValueGoalManager.class);
        when(goalManagerFactory_.createGoalManager(randomGoalData)).thenReturn(mockGoalManager);
        Long goalId = patient_.addGoal(randomGoalData);

        try {
            I_GoalNotifier<FakeTimestampedValue> retrievedUpdateNotifier = patient_.goalUpdateNotifierByGoalId(goalId);
            assertSame(mockGoalManager, retrievedUpdateNotifier);
        } catch (InvalidQueryException e) {
            fail("Caught unexpected exception");
        }
    }

    @Test
    public void willThrowExceptionWhenFetchingUpdateNotifierWithNonExistentId() {
        injectRandomGoalsIntoPatient();
        Long nonExistentGoalId = new RandomOther<>(new RandomLong()).otherThan(patient_.currentGoalIds());

        assertThrows(InvalidQueryException.class, () -> patient_.goalUpdateNotifierByGoalId(nonExistentGoalId));
    }

    @Test
    public void willReturnCorrectGoalUpdater() {
        injectRandomGoalsIntoPatient();
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        I_GoalManager<FakeTimestampedValue> mockGoalManager = mock(I_FakeValueGoalManager.class);
        when(goalManagerFactory_.createGoalManager(randomGoalData)).thenReturn(mockGoalManager);
        Long goalId = patient_.addGoal(randomGoalData);

        try {
            I_GoalUpdater<FakeTimestampedValue> retrievedGoalUpdater = patient_.goalUpdaterByGoalId(goalId);
            assertSame(mockGoalManager, retrievedGoalUpdater);
        } catch (InvalidQueryException e) {
            fail("Caught unexpected exception");
        }
    }

    @Test
    public void willThrowExceptionWhenFetchingGoalUpdaterWithNonExistentName() {
        injectRandomGoalsIntoPatient();
        Long nonExistentGoalId = new RandomOther<>(new RandomLong()).otherThan(patient_.currentGoalIds());

        assertThrows(InvalidQueryException.class, () -> patient_.goalUpdaterByGoalId(nonExistentGoalId));
    }

    @Test
    public void willDoNothingWhenRegisteringSameListenerSecondTime() {
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[0]);
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();

        Long goalId = patient_.addGoal(randomGoalData);

        verify(listenerList_[0], times(1)).goalAdded(goalId);

    }

    @Test
    public void willNotifyListenersWhenGoalSuccessfullyAdded() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        Long goalId = patient_.addGoal(randomGoalData);


        verify(listenerList_[0], times(1)).goalAdded(goalId);
        verify(listenerList_[1], times(1)).goalAdded(goalId);
    }

    @Test
    public void willOnlyNotifyListenersWhenGoalSuccessfullyRemoved() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);
        Long goalId = patient_.addGoal(randomGoalData);

        failOnException(() -> patient_.removeGoal(goalId));
        // second call to removeGoal should fail
        ignoreAnyExceptions(() -> patient_.removeGoal(goalId));

        verify(listenerList_[0], times(1)).goalRemoved(goalId);
        verify(listenerList_[1], times(1)).goalRemoved(goalId);
    }

    @Test
    public void willNotNotifyUnregisteredListener() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.unregisterListener(listenerList_[0]);

        Long goalId = patient_.addGoal(randomGoalData);

        verify(listenerList_[0], times(0)).goalAdded(goalId);

    }

    @Test
    public void willOnlyUnregisterRequestedListener() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);
        // only the 0th listener is removed. 1st listener remains
        patient_.unregisterListener(listenerList_[0]);

        Long goalId = patient_.addGoal(randomGoalData);

        verify(listenerList_[1], times(1)).goalAdded(goalId);
    }

    @Test
    public void willNotInitializeIfRegistryNotEmpty() {
        patient_.addGoal(randomGoalDataGenerator_.generate());
        HashMap<Long, GoalData<FakeTimestampedValue>> goalsFromPersistenceSource = new RandomHashMap<>(new RandomLong(),
                randomGoalDataGenerator_).generate();

        patient_.initializeWithExistingGoals(goalsFromPersistenceSource);

        assertEquals(1, patient_.currentGoalIds().size());
    }

    @Test
    public void willRequestGoalManagersWhenInitializing() {
        HashMap<Long, GoalData<FakeTimestampedValue>> goalsFromPersistenceSource = new RandomHashMap<>(new RandomLong(),
                randomGoalDataGenerator_).generate();

        patient_.initializeWithExistingGoals(goalsFromPersistenceSource);

        for (GoalData<FakeTimestampedValue> goalData : goalsFromPersistenceSource.values()) {
            verify(goalManagerFactory_, times(1)).createGoalManager(goalData);
        }
    }

    @Test
    public void willReturnCorrectGoalUpdatersAfterInitializing() {
        HashMap<Long, GoalData<FakeTimestampedValue>> goalsFromPersistenceSource = new RandomHashMap<>(new RandomLong(),
                randomGoalDataGenerator_).generate();
        HashMap<Long, I_GoalManager<FakeTimestampedValue>> expectedGoalManagersByGoalId = new HashMap<>();
        for (Map.Entry<Long, GoalData<FakeTimestampedValue>> idAndGoalData : goalsFromPersistenceSource.entrySet()) {
            I_GoalManager<FakeTimestampedValue> mockGoalManager = mock(I_FakeValueGoalManager.class);
            when(goalManagerFactory_.createGoalManager(idAndGoalData.getValue())).thenReturn(mockGoalManager);
            expectedGoalManagersByGoalId.put(idAndGoalData.getKey(), mockGoalManager);
        }

        patient_.initializeWithExistingGoals(goalsFromPersistenceSource);

        for (Map.Entry<Long, I_GoalManager<FakeTimestampedValue>> idAndExpectedManagers : expectedGoalManagersByGoalId.entrySet()) {
            Long goalId = idAndExpectedManagers.getKey();
            I_GoalUpdater<FakeTimestampedValue> expectedGoalUpdater = idAndExpectedManagers.getValue();
            try {
                I_GoalUpdater<FakeTimestampedValue> actualGoalUpdater = patient_.goalUpdaterByGoalId(goalId);
                assertSame(expectedGoalUpdater, actualGoalUpdater);
            } catch (InvalidQueryException e) {
                fail("Caught unexpected exception");
            }
        }
    }

    @Test
    public void willReturnCorrectGoalIdsAfterInitializing() {
        HashMap<Long, GoalData<FakeTimestampedValue>> goalsFromPersistenceSource = new RandomHashMap<>(new RandomLong(),
                randomGoalDataGenerator_).generate();
        HashSet<Long> expectedGoalIds = new HashSet<>(goalsFromPersistenceSource.keySet());

        patient_.initializeWithExistingGoals(goalsFromPersistenceSource);

        assertEquals(expectedGoalIds, patient_.currentGoalIds());
    }

    @Test
    public void willCorrectlyIncrementGoalIdsAfterInitializing() {
        HashMap<Long, GoalData<FakeTimestampedValue>> goalsFromPersistenceSource = new RandomHashMap<>(new RandomLong(),
                randomGoalDataGenerator_).generate();
        Long expectedNextGoalId = Collections.max(goalsFromPersistenceSource.keySet()) + 1L;

        patient_.initializeWithExistingGoals(goalsFromPersistenceSource);

        assertEquals(expectedNextGoalId, patient_.addGoal(randomGoalDataGenerator_.generate()));
    }

    private void prepareGoalManagerFactoryToCreateGoal(GoalData<FakeTimestampedValue> goalData) {
        I_GoalManager<FakeTimestampedValue> mockGoalManager = mock(I_FakeValueGoalManager.class);
        when(goalManagerFactory_.createGoalManager(goalData)).thenReturn(mockGoalManager);
    }

    private void injectRandomGoalsIntoPatient() {
        int numberGoalsToInject = new RandomInt().generate(1, 10);
        ArrayList<GoalData<FakeTimestampedValue>> randomGoalDataList =
                new RandomArrayList<>(randomGoalDataGenerator_).generate(numberGoalsToInject);
        for (GoalData<FakeTimestampedValue> goal : randomGoalDataList) {
            prepareGoalManagerFactoryToCreateGoal(goal);
            patient_.addGoal(goal);
        }
    }

    // java requires a functional interface to pass lambdas by argument
    private interface LambdaWithException {
        void fun() throws Exception;
    }

    private void failOnException(LambdaWithException lambda) {
        try {
            lambda.fun();
        } catch (Exception e) {
            fail("Caught unexpected exception" + e.getMessage());
        }
    }

    private void ignoreAnyExceptions(LambdaWithException lambda) {
        try {
            lambda.fun();
        } catch (Exception ignore) {
        }
    }

}