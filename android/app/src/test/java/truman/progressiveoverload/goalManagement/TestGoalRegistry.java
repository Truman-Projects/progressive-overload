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

import truman.progressiveoverload.goalManagement.api.DuplicateEntryException;
import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomArrayList;
import truman.progressiveoverload.randomUtilities.RandomInt;
import truman.progressiveoverload.randomUtilities.RandomOther;
import truman.progressiveoverload.randomUtilities.RandomString;

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
    public void willReturnNamesOfAllAddedGoals() {
        int numberOfGoals = new RandomInt().generate(1, 5);
        ArrayList<GoalData<FakeTimestampedValue>> randomGoalDataListWithUniqueNames =
                generateRandomGoalDataListWithUniqueName(numberOfGoals);
        HashSet<String> expectedGoalDataNames = new HashSet<>();
        for (GoalData<FakeTimestampedValue> goal : randomGoalDataListWithUniqueNames) {
            expectedGoalDataNames.add(goal.name());
            prepareGoalManagerFactoryToCreateGoal(goal);
            failOnException(() -> patient_.addGoal(goal));
        }

        HashSet<String> queriedGoalDataNames = patient_.currentGoalNames();

        assertEquals(expectedGoalDataNames, queriedGoalDataNames);
    }

    @Test
    public void willNotReturnNamesOfRemovedGoals() {
        final int NUMBER_OF_GOALS = 5;
        ArrayList<GoalData<FakeTimestampedValue>> randomGoalDataListWithUniqueNames =
                generateRandomGoalDataListWithUniqueName(NUMBER_OF_GOALS);
        HashSet<String> goalDataNames = new HashSet<>();
        for (GoalData<FakeTimestampedValue> goal : randomGoalDataListWithUniqueNames) {
            goalDataNames.add(goal.name());
            prepareGoalManagerFactoryToCreateGoal(goal);
            failOnException(() -> patient_.addGoal(goal));
        }
        String[] goalNamesAsList = goalDataNames.toArray(new String[NUMBER_OF_GOALS]);
        int indexInGoalNameListToRemove = new RandomInt().generate(0, NUMBER_OF_GOALS);
        String goalNameToRemove = goalNamesAsList[indexInGoalNameListToRemove];
        failOnException(() -> patient_.removeGoal(goalNameToRemove));
        goalDataNames.remove(goalNameToRemove);

        HashSet<String> queriedGoalDataNames = patient_.currentGoalNames();

        assertEquals(goalDataNames, queriedGoalDataNames);
    }

    @Test
    public void willThrowExceptionWhenRemovingNonExistentGoalName() {
        injectRandomGoalsIntoPatient();
        String nonExistentGoalName = new RandomOther<>(new RandomString()).otherThan(patient_.currentGoalNames());

        assertThrows(InvalidQueryException.class, () -> patient_.removeGoal(nonExistentGoalName));

    }

    @Test
    public void willCallCreateGoalManagerWhenAddingGoal() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);

        failOnException(() -> patient_.addGoal(randomGoalData));

        verify(goalManagerFactory_, times(1)).createGoalManager(randomGoalData);
    }

    @Test
    public void willThrowExceptionWhenAddingGoalWithExistingGoalName() {
        String goalName = new RandomString().generate();
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate().withName(goalName);
        GoalData<FakeTimestampedValue> randomGoalDataWithRepeatedName = randomGoalDataGenerator_.generate().withName(goalName);
        failOnException(() -> patient_.addGoal(randomGoalData));

        assertThrows(DuplicateEntryException.class, () -> patient_.addGoal(randomGoalDataWithRepeatedName));

    }

    @Test
    public void willReturnCorrectGoalUpdateNotifier() {
        injectRandomGoalsIntoPatient();
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        I_GoalManager<FakeTimestampedValue> mockGoalManager = mock(I_FakeValueGoalManager.class);
        when(goalManagerFactory_.createGoalManager(randomGoalData)).thenReturn(mockGoalManager);
        failOnException(() -> patient_.addGoal(randomGoalData));

        try {
            I_GoalNotifier<FakeTimestampedValue> retrievedUpdateNotifier = patient_.goalUpdateNotifierByName(randomGoalData.name());
            assertSame(mockGoalManager, retrievedUpdateNotifier);
        } catch (InvalidQueryException e) {
            fail("Caught unexpected exception");
        }
    }

    @Test
    public void willThrowExceptionWhenFetchingUpdateNotifierWithNonExistentName() {
        injectRandomGoalsIntoPatient();
        String nonExistentGoalName = new RandomOther<>(new RandomString()).otherThan(patient_.currentGoalNames());

        assertThrows(InvalidQueryException.class, () -> patient_.goalUpdateNotifierByName(nonExistentGoalName));
    }

    @Test
    public void willReturnCorrectGoalUpdater() {
        injectRandomGoalsIntoPatient();
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        I_GoalManager<FakeTimestampedValue> mockGoalManager = mock(I_FakeValueGoalManager.class);
        when(goalManagerFactory_.createGoalManager(randomGoalData)).thenReturn(mockGoalManager);
        failOnException(() -> patient_.addGoal(randomGoalData));

        try {
            I_GoalUpdater<FakeTimestampedValue> retrievedGoalUpdater = patient_.goalUpdaterByName(randomGoalData.name());
            assertSame(mockGoalManager, retrievedGoalUpdater);
        } catch (InvalidQueryException e) {
            fail("Caught unexpected exception");
        }
    }

    @Test
    public void willThrowExceptionWhenFetchingGoalUpdaterWithNonExistentName() {
        injectRandomGoalsIntoPatient();
        String nonExistentGoalName = new RandomOther<>(new RandomString()).otherThan(patient_.currentGoalNames());

        assertThrows(InvalidQueryException.class, () -> patient_.goalUpdaterByName(nonExistentGoalName));
    }

    @Test
    public void willDoNothingWhenRegisteringSameListenerSecondTime() {
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[0]);
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();

        failOnException(() -> patient_.addGoal(randomGoalData));

        verify(listenerList_[0], times(1)).goalAdded(randomGoalData.name());

    }

    @Test
    public void willOnlyNotifyListenersWhenGoalSuccessfullyAdded() {
        String goalName = new RandomString().generate();
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate().withName(goalName);
        GoalData<FakeTimestampedValue> randomGoalDataWithRepeatedName = randomGoalDataGenerator_.generate().withName(goalName);
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        failOnException(() -> patient_.addGoal(randomGoalData));
        // second call to addGoal should fail
        ignoreAnyExceptions(() -> patient_.addGoal(randomGoalDataWithRepeatedName));


        verify(listenerList_[0], times(1)).goalAdded(goalName);
        verify(listenerList_[1], times(1)).goalAdded(goalName);
    }

    @Test
    public void willOnlyNotifyListenersWhenGoalSuccessfullyRemoved() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);
        failOnException(() -> patient_.addGoal(randomGoalData));

        failOnException(() -> patient_.removeGoal(randomGoalData.name()));
        // second call to removeGoal should fail
        ignoreAnyExceptions(() -> patient_.removeGoal(randomGoalData.name()));

        verify(listenerList_[0], times(1)).goalRemoved(randomGoalData.name());
        verify(listenerList_[1], times(1)).goalRemoved(randomGoalData.name());
    }

    @Test
    public void willNotNotifyUnregisteredListener() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.unregisterListener(listenerList_[0]);

        failOnException(() -> patient_.addGoal(randomGoalData));

        verify(listenerList_[0], times(0)).goalAdded(randomGoalData.name());

    }

    @Test
    public void willOnlyUnregisterRequestedListener() {
        GoalData<FakeTimestampedValue> randomGoalData = randomGoalDataGenerator_.generate();
        prepareGoalManagerFactoryToCreateGoal(randomGoalData);
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);
        // only the 0th listener is removed. 1st listener remains
        patient_.unregisterListener(listenerList_[0]);

        failOnException(() -> patient_.addGoal(randomGoalData));

        verify(listenerList_[1], times(1)).goalAdded(randomGoalData.name());
    }


    private ArrayList<GoalData<FakeTimestampedValue>> generateRandomGoalDataListWithUniqueName(int size) {
        ArrayList<String> uniqueGoalNames = new ArrayList<>();
        ArrayList<GoalData<FakeTimestampedValue>> goalDataListWithUniqueNames = new ArrayList<>();

        RandomOther<String> uniqueStringGenerator = new RandomOther<>(new RandomString());
        ArrayList<GoalData<FakeTimestampedValue>> randomGoalDataList = new RandomArrayList<>(randomGoalDataGenerator_).generate(size);
        for (int index = 0; index < size; index++) {
            String goalNameForIndex = uniqueStringGenerator.otherThan(uniqueGoalNames);
            uniqueGoalNames.add(goalNameForIndex);
            goalDataListWithUniqueNames.add(randomGoalDataList.get(index).withName(goalNameForIndex));
        }

        return goalDataListWithUniqueNames;
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
            ignoreAnyExceptions(() -> patient_.addGoal(goal));
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