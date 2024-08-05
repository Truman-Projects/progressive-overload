package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.randomUtilities.RandomArrayList;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomInt;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomOther;

class TestGoalRegistry {
    // intermediate interfaces to appease the mockito gods

    private interface I_FakeValueGoalManagerFactory extends I_GoalManagerFactory<Long> {
    }

    private interface I_FakeValueGoalManager extends I_GoalManager<Long> {
    }


    private final RandomGoalData<Long> randomGoalDataGenerator_ = new RandomGoalData<>(new RandomLong());
    private I_GoalRegistryListener[] listenerList_;
    private HashMap<Long, GoalData<Long>> goalsByIdFromPersistence_;
    private I_FakeValueGoalManagerFactory goalManagerFactory_;
    private I_RandomLongGenerator rng_;
    private UniqueIdSource idSource_;
    private GoalRegistry<Long> patient_;

    @BeforeEach
    public void resetEverything() {
        listenerList_ = new I_GoalRegistryListener[]{
                mock(I_GoalRegistryListener.class),
                mock(I_GoalRegistryListener.class)};
        goalsByIdFromPersistence_ = new HashMap<>();
        goalManagerFactory_ = mock(I_FakeValueGoalManagerFactory.class);
        // using concrete dependencies because UniqueIdSource does not have an interface
        rng_ = new RandomLongGenerator();
        idSource_ = new UniqueIdSource(rng_);
        prepareMocksForGoalsFromPersistence();
        recreatePatient();
    }

    private void prepareMocksForGoalsFromPersistence() {
        for (GoalData<Long> goalData : goalsByIdFromPersistence_.values()) {
            prepareGoalManagerFactoryToCreateGoal(goalData);
        }
    }

    private void recreatePatient() {
        patient_ = new GoalRegistry<>(goalsByIdFromPersistence_, goalManagerFactory_, idSource_);
    }

    @Test
    public void willReserveGoalIdsFromPersistence() {
        goalsByIdFromPersistence_ =
                new RandomHashMap<>(new RandomLong(), randomGoalDataGenerator_).generate();
        prepareMocksForGoalsFromPersistence();
        recreatePatient();

        for (Long goalIdFromPatient : goalsByIdFromPersistence_.keySet()) {
            // should not be able to reserve ID already taken by patient
            assertFalse(idSource_.attemptToReserveId(goalIdFromPatient));
        }
    }

    @Test
    public void willCreateGoalManagerForGoalsFromPersistence() {
        goalsByIdFromPersistence_ =
                new RandomHashMap<>(new RandomLong(), new RandomGoalData<>(new RandomLong())).generate();
        prepareMocksForGoalsFromPersistence();

        recreatePatient();

        for (GoalData<Long> goalDataFromPersistence : goalsByIdFromPersistence_.values()) {
            verify(goalManagerFactory_, times(1)).createGoalManager(goalDataFromPersistence);
        }
    }

    @Test
    public void willReturnGoalUpdaterForGoalsFromPersistence() {
        Long goalIdFromPersistence = new RandomLong().generate();
        GoalData<Long> goalDataFromPersistence = randomGoalDataGenerator_.generate();
        goalsByIdFromPersistence_.put(goalIdFromPersistence, goalDataFromPersistence);
        I_GoalUpdater<Long> expectedGoalUpdater = prepareGoalManagerFactoryToCreateGoal(goalDataFromPersistence);

        recreatePatient();

        failOnException(() -> {
            I_GoalUpdater<Long> queriedGoalUpdater = patient_.goalUpdaterByGoalId(goalIdFromPersistence);
            assertSame(expectedGoalUpdater, queriedGoalUpdater);
        });
    }

    @Test
    public void willNotAddGoalFromPersistenceIfIdNotAvailable() {
        Long goalIdFromPersistence = new RandomLong().generate();
        idSource_.attemptToReserveId(goalIdFromPersistence);
        GoalData<Long> randomGoalDataFromPersistence = randomGoalDataGenerator_.generate();
        goalsByIdFromPersistence_.put(goalIdFromPersistence, randomGoalDataFromPersistence);

        recreatePatient();

        assertFalse(patient_.currentGoalIds().contains(goalIdFromPersistence));

    }

    @Test
    public void willReturnIdsOfAllAddedGoals() {
        int numberOfGoals = new RandomInt().generate(1, 5);
        ArrayList<GoalData<Long>> randomGoalDataList =
                new RandomArrayList<>(randomGoalDataGenerator_).generate(numberOfGoals);
        HashSet<Long> expectedGoalDataIDs = new HashSet<>();
        for (GoalData<Long> goal : randomGoalDataList) {
            prepareGoalManagerFactoryToCreateGoal(goal);
            Long goalId = patient_.addGoal(goal);
            expectedGoalDataIDs.add(goalId);
        }

        HashSet<Long> queriedGoalDataIDs = patient_.currentGoalIds();

        assertEquals(expectedGoalDataIDs, queriedGoalDataIDs);
    }

    @Test
    public void willReserveNewGoalIdsInIdSource() {
        int numberOfGoals = new RandomInt().generate(1, 5);
        ArrayList<GoalData<Long>> randomGoalDataList =
                new RandomArrayList<>(randomGoalDataGenerator_).generate(numberOfGoals);
        for (GoalData<Long> goal : randomGoalDataList) {
            prepareGoalManagerFactoryToCreateGoal(goal);
            patient_.addGoal(goal);
        }

        HashSet<Long> goalIdsAccordingToPatient = patient_.currentGoalIds();

        for (Long goalIdFromPatient : goalIdsAccordingToPatient) {
            // should not be able to reserve ID already taken by patient
            assertFalse(idSource_.attemptToReserveId(goalIdFromPatient));
        }

    }

    @Test
    public void willNotReturnIDsOfRemovedGoals() {
        final int NUMBER_OF_GOALS = 5;
        ArrayList<GoalData<Long>> randomGoalDataList =
                new RandomArrayList<>(randomGoalDataGenerator_).generate(NUMBER_OF_GOALS);
        HashSet<Long> goalIDs = new HashSet<>();
        for (GoalData<Long> goal : randomGoalDataList) {
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
        GoalData<Long> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);

        patient_.addGoal(randomGoalData);

        verify(goalManagerFactory_, times(1)).createGoalManager(randomGoalData);
    }

    @Test
    public void willNotReuseRemovedGoalIds() {
        GoalData<Long> randomGoal1 = randomGoalDataGenerator_.generate();
        GoalData<Long> randomGoal2 = randomGoalDataGenerator_.generate();
        Long firstGoalId = patient_.addGoal(randomGoal1);
        failOnException(() -> patient_.removeGoal(firstGoalId));

        Long secondGoalId = patient_.addGoal(randomGoal2);

        assertNotEquals(firstGoalId, secondGoalId);
    }


    @Test
    public void willReturnCorrectGoalUpdateNotifier() {
        injectRandomGoalsIntoPatient();
        GoalData<Long> randomGoalData = randomGoalDataGenerator_.generate();
        I_GoalManager<Long> mockGoalManager = prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        Long goalId = patient_.addGoal(randomGoalData);

        try {
            I_GoalNotifier<Long> retrievedUpdateNotifier = patient_.goalUpdateNotifierByGoalId(goalId);
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
        GoalData<Long> randomGoalData = randomGoalDataGenerator_.generate();
        I_GoalManager<Long> mockGoalManager = prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        Long goalId = patient_.addGoal(randomGoalData);

        failOnException(() -> {
            I_GoalUpdater<Long> retrievedGoalUpdater = patient_.goalUpdaterByGoalId(goalId);
            assertSame(mockGoalManager, retrievedGoalUpdater);
        });
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
        GoalData<Long> randomGoalData = randomGoalDataGenerator_.generate();

        Long goalId = patient_.addGoal(randomGoalData);

        verify(listenerList_[0], times(1)).goalAdded(goalId);

    }

    @Test
    public void willNotifyListenersWhenGoalSuccessfullyAdded() {
        GoalData<Long> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        Long goalId = patient_.addGoal(randomGoalData);


        verify(listenerList_[0], times(1)).goalAdded(goalId);
        verify(listenerList_[1], times(1)).goalAdded(goalId);
    }

    @Test
    public void willOnlyNotifyListenersWhenGoalSuccessfullyRemoved() {
        GoalData<Long> randomGoalData = randomGoalDataGenerator_.generate();
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
        GoalData<Long> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.unregisterListener(listenerList_[0]);

        Long goalId = patient_.addGoal(randomGoalData);

        verify(listenerList_[0], times(0)).goalAdded(goalId);

    }

    @Test
    public void willOnlyUnregisterRequestedListener() {
        GoalData<Long> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);
        // only the 0th listener is removed. 1st listener remains
        patient_.unregisterListener(listenerList_[0]);

        Long goalId = patient_.addGoal(randomGoalData);

        verify(listenerList_[1], times(1)).goalAdded(goalId);
    }

    private I_GoalManager<Long> prepareGoalManagerFactoryToCreateGoal(GoalData<Long> goalData) {
        I_GoalManager<Long> mockGoalManager = mock(I_FakeValueGoalManager.class);
        when(goalManagerFactory_.createGoalManager(goalData)).thenReturn(mockGoalManager);

        return mockGoalManager;
    }

    private void injectRandomGoalsIntoPatient() {
        int numberGoalsToInject = new RandomInt().generate(1, 10);
        ArrayList<GoalData<Long>> randomGoalDataList =
                new RandomArrayList<>(randomGoalDataGenerator_).generate(numberGoalsToInject);
        for (GoalData<Long> goal : randomGoalDataList) {
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