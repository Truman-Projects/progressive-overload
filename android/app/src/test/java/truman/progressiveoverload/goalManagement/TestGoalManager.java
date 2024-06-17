package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.GoalType;
import truman.progressiveoverload.goalManagement.api.I_GoalListener;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.measurement.RandomFakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomInt;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomOther;
import truman.progressiveoverload.randomUtilities.RandomString;

class TestGoalManager {

    // intermediate interface to appease the mockito gods
    private interface I_FakeValueGoalListener extends I_GoalListener<FakeTimestampedValue> {
    }

    private static final RandomGoalData goalDataGen_ = new RandomGoalData();
    private I_FakeValueGoalListener[] listenerList_;
    private GoalData<FakeTimestampedValue> initialGoalData_;
    private GoalManager<FakeTimestampedValue> patient_;

    @BeforeEach
    public void resetEverything() {
        listenerList_ = new I_FakeValueGoalListener[]{
                mock(I_FakeValueGoalListener.class),
                mock(I_FakeValueGoalListener.class)
        };
        initialGoalData_ = goalDataGen_.generate();
        resetPatient();
    }

    private void resetPatient() {
        patient_ = new GoalManager<>(initialGoalData_);
    }

    @Test
    public void willProvideCorrectCurrentStateOnConstruction() {

        assertEquals(initialGoalData_, patient_.currentState());
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterChangingDescription() {
        String newDescription = new RandomOther<>(new RandomString()).otherThan(initialGoalData_.description());
        GoalData<FakeTimestampedValue> goalDataWithNewDescription = initialGoalData_.withDescription(newDescription);

        patient_.changeGoalDescription(newDescription);

        assertEquals(goalDataWithNewDescription, patient_.currentState());
    }

    @ParameterizedTest
    @MethodSource("willProvideUpdatedCurrentStateAfterChangingGoalType_data")
    public void willProvideUpdatedCurrentStateAfterChangingGoalType(GoalType initialGoalType, GoalType newGoalType) {
        initialGoalData_ = initialGoalData_.withGoalType(initialGoalType);
        resetPatient();
        GoalData<FakeTimestampedValue> goalDataWithNewGoalType = initialGoalData_.withGoalType(newGoalType);

        patient_.changeGoalType(newGoalType);

        assertEquals(goalDataWithNewGoalType, patient_.currentState());
    }

    // returns (GoalType initialGoalType, GoalType newGoalType)
    private static Stream<Arguments> willProvideUpdatedCurrentStateAfterChangingGoalType_data() {
        return Stream.of(
                Arguments.of(GoalType.MINIMIZE, GoalType.MAXIMIZE), // min to max
                Arguments.of(GoalType.MAXIMIZE, GoalType.MINIMIZE), // max to min
                Arguments.of(GoalType.MINIMIZE, GoalType.MINIMIZE), // min, no change
                Arguments.of(GoalType.MAXIMIZE, GoalType.MAXIMIZE) // max, no change
        );
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterAddingRecord() {
        FakeTimestampedValue newRecord = new RandomFakeTimestampedValue().generate();
        HashMap<Long, FakeTimestampedValue> initialRecords = initialGoalData_.recordsById();

        Long newRecordId = patient_.addRecord(newRecord);

        HashMap<Long, FakeTimestampedValue> expectedRecords = new HashMap<>(initialRecords);
        expectedRecords.put(newRecordId, newRecord);
        GoalData<FakeTimestampedValue> goalDataWithExpectedRecords = initialGoalData_.withRecordsById(expectedRecords);
        assertEquals(goalDataWithExpectedRecords, patient_.currentState());
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterAddingMilestone() {
        FakeTimestampedValue newMilestone = new RandomFakeTimestampedValue().generate();
        HashMap<Long, FakeTimestampedValue> initialMilestones = initialGoalData_.targetMilestonesById();

        Long newMilestoneId = patient_.addTargetMilestone(newMilestone);

        HashMap<Long, FakeTimestampedValue> expectedMilestones = new HashMap<>(initialMilestones);
        expectedMilestones.put(newMilestoneId, newMilestone);
        GoalData<FakeTimestampedValue> goalDataWithExpectedMilestones = initialGoalData_.withTargetMilestonesById(expectedMilestones);
        assertEquals(goalDataWithExpectedMilestones, patient_.currentState());
    }

    @ParameterizedTest
    @MethodSource("idOrderingTestData")
    public void willGenerateRecordIdsInIncreasingOrder(HashMap<Long, FakeTimestampedValue> initialMap, int numberOfEntriesToAdd,
                                                       Set<Long> expectedIds) {
        initialGoalData_ = initialGoalData_.withRecordsById(initialMap);
        resetPatient();
        RandomFakeTimestampedValue valueGenerator = new RandomFakeTimestampedValue();

        for (int recordsAdded = 0; recordsAdded < numberOfEntriesToAdd; recordsAdded++) {
            FakeTimestampedValue randomValue = valueGenerator.generate();
            patient_.addRecord(randomValue);
        }

        Set<Long> actualIds = patient_.currentState().recordsById().keySet();
        assertEquals(expectedIds, actualIds);
    }

    @ParameterizedTest
    @MethodSource("idOrderingTestData")
    public void willGenerateMilestoneIdsInIncreasingOrder(HashMap<Long, FakeTimestampedValue> initialMap, int numberOfEntriesToAdd,
                                                          Set<Long> expectedIds) {
        initialGoalData_ = initialGoalData_.withTargetMilestonesById(initialMap);
        resetPatient();
        RandomFakeTimestampedValue valueGenerator = new RandomFakeTimestampedValue();

        for (int milestonesAdded = 0; milestonesAdded < numberOfEntriesToAdd; milestonesAdded++) {
            FakeTimestampedValue randomValue = valueGenerator.generate();
            patient_.addTargetMilestone(randomValue);
        }

        Set<Long> actualIds = patient_.currentState().targetMilestonesById().keySet();
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterRemovingValidRecord() {
        HashMap<Long, FakeTimestampedValue> initialRecords = initialGoalData_.recordsById();
        Long recordIdToRemove = pickRandomEntryIdFromMap(initialRecords);

        try {
            patient_.removeRecord(recordIdToRemove);
        } catch (InvalidQueryException ignore) {
        }

        HashMap<Long, FakeTimestampedValue> expectedRecords = new HashMap<>(initialRecords);
        expectedRecords.remove(recordIdToRemove);
        GoalData<FakeTimestampedValue> goalDataWithExpectedRecords = initialGoalData_.withRecordsById(expectedRecords);
        assertEquals(goalDataWithExpectedRecords, patient_.currentState());
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterRemovingValidMilestone() {
        HashMap<Long, FakeTimestampedValue> initialMilestones = initialGoalData_.targetMilestonesById();
        Long milestoneIdToRemove = pickRandomEntryIdFromMap(initialMilestones);

        try {
            patient_.removeTargetMilestone(milestoneIdToRemove);
        } catch (InvalidQueryException ignore) {
        }

        HashMap<Long, FakeTimestampedValue> expectedMilestones = new HashMap<>(initialMilestones);
        expectedMilestones.remove(milestoneIdToRemove);
        GoalData<FakeTimestampedValue> goalDataWithExpectedMilestones = initialGoalData_.withTargetMilestonesById(expectedMilestones);
        assertEquals(goalDataWithExpectedMilestones, patient_.currentState());
    }

    @Test
    public void willThrowExceptionWhenAttemptingToRemoveInvalidRecordId() {
        Long invalidRecordId = pickRandomEntryIdNotInMap(initialGoalData_.recordsById());

        assertThrows(InvalidQueryException.class, () -> patient_.removeRecord(invalidRecordId));
    }

    @Test
    public void willThrowExceptionWhenAttemptingToRemoveInvalidMilestoneId() {
        Long invalidMilestoneId = pickRandomEntryIdNotInMap(initialGoalData_.recordsById());

        assertThrows(InvalidQueryException.class, () -> patient_.removeTargetMilestone(invalidMilestoneId));
    }

    @Test
    public void willNotReuseRemovedRecordIds() {
        initialGoalData_ = initialGoalData_.withRecordsById(new HashMap<>());
        resetPatient();
        RandomFakeTimestampedValue valueGen = new RandomFakeTimestampedValue();
        Long firstRecordId = patient_.addRecord(valueGen.generate());
        try {
            patient_.removeRecord(firstRecordId);
        } catch (InvalidQueryException ignore) {
        }

        Long secondRecordId = patient_.addRecord(valueGen.generate());

        assertNotEquals(firstRecordId, secondRecordId);
    }

    @Test
    public void willNotReuseRemovedMilestoneIds() {
        initialGoalData_ = initialGoalData_.withTargetMilestonesById(new HashMap<>());
        resetPatient();
        RandomFakeTimestampedValue valueGen = new RandomFakeTimestampedValue();
        Long firstMilestoneId = patient_.addTargetMilestone(valueGen.generate());
        try {
            patient_.removeTargetMilestone(firstMilestoneId);
        } catch (InvalidQueryException ignore) {
        }

        Long secondMilestoneId = patient_.addTargetMilestone(valueGen.generate());

        assertNotEquals(firstMilestoneId, secondMilestoneId);
    }

    @Test
    public void willProvideCorrectCurrentStateAfterEditingRecord() {
        HashMap<Long, FakeTimestampedValue> initialRecords = initialGoalData_.recordsById();
        Long recordIdToEdit = pickRandomEntryIdFromMap(initialRecords);
        FakeTimestampedValue recordAfterEditing =
                new RandomOther<>(new RandomFakeTimestampedValue()).otherThan(new ArrayList<>(initialRecords.values()));

        try {
            patient_.editRecord(recordIdToEdit, recordAfterEditing);
        } catch (InvalidQueryException ignore) {
        }

        assertEquals(recordAfterEditing, patient_.currentState().recordsById().get(recordIdToEdit));
    }

    @Test
    public void willProvideCorrectCurrentStateAfterEditingMilestone() {
        HashMap<Long, FakeTimestampedValue> initialMilestones = initialGoalData_.targetMilestonesById();
        Long milestoneIdToEdit = pickRandomEntryIdFromMap(initialMilestones);
        FakeTimestampedValue milestoneAfterEditing =
                new RandomOther<>(new RandomFakeTimestampedValue()).otherThan(new ArrayList<>(initialMilestones.values()));

        try {
            patient_.editTargetMilestone(milestoneIdToEdit, milestoneAfterEditing);
        } catch (InvalidQueryException ignore) {
        }

        assertEquals(milestoneAfterEditing, patient_.currentState().targetMilestonesById().get(milestoneIdToEdit));
    }

    @Test
    public void willThrowExceptionWhenAttemptingToEditInvalidRecordId() {
        Long invalidRecordId = pickRandomEntryIdNotInMap(initialGoalData_.recordsById());
        FakeTimestampedValue randomUpdatedRecord = new RandomFakeTimestampedValue().generate();

        assertThrows(InvalidQueryException.class, () -> patient_.editRecord(invalidRecordId, randomUpdatedRecord));
    }

    @Test
    public void willThrowExceptionWhenAttemptingToEditInvalidMilestoneId() {
        Long invalidMilestoneId = pickRandomEntryIdNotInMap(initialGoalData_.targetMilestonesById());
        FakeTimestampedValue randomUpdatedMilestone = new RandomFakeTimestampedValue().generate();

        assertThrows(InvalidQueryException.class, () -> patient_.editTargetMilestone(invalidMilestoneId, randomUpdatedMilestone));
    }

    @Test
    public void willDoNothingIfListenerRegisteredTwice() {
        patient_.registerListener(listenerList_[0]);
        String updatedDescription = new RandomString().generate();

        // register the same listener again
        patient_.registerListener(listenerList_[0]);
        // we are only testing changeGoalDescription() as it would be impractical to test this for every single changed signal
        patient_.changeGoalDescription(updatedDescription);

        verify(listenerList_[0], times(1)).goalDescriptionChanged(updatedDescription);
    }

    @Test
    public void willNotNotifyUnregisteredListener() {
        patient_.registerListener(listenerList_[0]);
        String updatedDescription = new RandomString().generate();
        patient_.changeGoalDescription(updatedDescription);

        patient_.unregisterListener(listenerList_[0]);
        // we are only testing changeGoalDescription() as it would be impractical to test this for every single changed signal
        patient_.changeGoalDescription(updatedDescription);

        verify(listenerList_[0], times(1)).goalDescriptionChanged(updatedDescription);
    }

    @Test
    public void willDoNothingWhenUnregisteringUnknownListener() {
        patient_.registerListener(listenerList_[0]);
        String updatedDescription = new RandomString().generate();


        // this listener was never registered
        patient_.unregisterListener(listenerList_[1]);
        // we are only testing changeGoalDescription() as it would be impractical to test this for every single changed signal
        patient_.changeGoalDescription(updatedDescription);

        verify(listenerList_[0], times(1)).goalDescriptionChanged(updatedDescription);
    }

    @ParameterizedTest
    @MethodSource("willNotifyListenersWhenDescriptionChanged_data")
    public void willNotifyListenersWhenDescriptionChanged(String initialDescription, String updatedDescription,
                                                          int timesDescriptionChanged) {
        initialGoalData_ = initialGoalData_.withDescription(initialDescription);
        resetPatient();
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        patient_.changeGoalDescription(updatedDescription);

        verify(listenerList_[0], times(timesDescriptionChanged)).goalDescriptionChanged(updatedDescription);
        verify(listenerList_[1], times(timesDescriptionChanged)).goalDescriptionChanged(updatedDescription);
    }

    // returns (String initialDescription, String updatedDescription, int timesDescriptionChanged)
    private static Stream<Arguments> willNotifyListenersWhenDescriptionChanged_data() {
        RandomString stringGen = new RandomString();
        String string1 = stringGen.generate();
        String string2 = new RandomOther<>(stringGen).otherThan(string1);

        return Stream.of(
                Arguments.of(string1, string1, 0),
                Arguments.of(string1, string2, 1)
        );
    }


    @ParameterizedTest
    @MethodSource("willNotifyListenersWhenGoalTypeChanged_data")
    public void willNotifyListenersWhenGoalTypeChanged(GoalType initialGoalType, GoalType updatedGoalType, int timesGoalTypeChanged) {
        initialGoalData_ = initialGoalData_.withGoalType(initialGoalType);
        resetPatient();
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        patient_.changeGoalType(updatedGoalType);

        verify(listenerList_[0], times(timesGoalTypeChanged)).goalTypeChanged(updatedGoalType);
        verify(listenerList_[1], times(timesGoalTypeChanged)).goalTypeChanged(updatedGoalType);
    }

    // returns (GoalType initialGoalType, GoalType updatedGoalType, int timesGoalTypeChanged)
    private static Stream<Arguments> willNotifyListenersWhenGoalTypeChanged_data() {
        return Stream.of(
                Arguments.of(GoalType.MINIMIZE, GoalType.MAXIMIZE, 1), // min to max
                Arguments.of(GoalType.MAXIMIZE, GoalType.MINIMIZE, 1), // max to min
                Arguments.of(GoalType.MINIMIZE, GoalType.MINIMIZE, 0), // min, no change
                Arguments.of(GoalType.MAXIMIZE, GoalType.MAXIMIZE, 0) // max, no change
        );
    }

    @Test
    public void willNotifyListenersWhenRecordAdded() {
        FakeTimestampedValue newRecord = new RandomFakeTimestampedValue().generate();
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        Long newRecordId = patient_.addRecord(newRecord);

        verify(listenerList_[0], times(1)).recordAdded(newRecordId, newRecord);
        verify(listenerList_[1], times(1)).recordAdded(newRecordId, newRecord);
    }

    @Test
    public void willNotifyListenersWhenMilestoneAdded() {
        FakeTimestampedValue newMilestone = new RandomFakeTimestampedValue().generate();
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        Long newMilestoneId = patient_.addTargetMilestone(newMilestone);

        verify(listenerList_[0], times(1)).targetMilestoneAdded(newMilestoneId, newMilestone);
        verify(listenerList_[1], times(1)).targetMilestoneAdded(newMilestoneId, newMilestone);
    }

    @ParameterizedTest
    @MethodSource("validAndInvalidHashMapIds")
    public void willNotifyListenersWhenRecordRemoved(HashMap<Long, FakeTimestampedValue> initialRecords, Long potentiallyInvalidRecordId,
                                                     boolean idIsValid) {
        initialGoalData_ = initialGoalData_.withRecordsById(initialRecords);
        resetPatient();
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        try {
            patient_.removeRecord(potentiallyInvalidRecordId);
        } catch (InvalidQueryException ignore) {
        }

        verify(listenerList_[0], times(idIsValid ? 1 : 0)).recordRemoved(potentiallyInvalidRecordId);
        verify(listenerList_[1], times(idIsValid ? 1 : 0)).recordRemoved(potentiallyInvalidRecordId);
    }

    @ParameterizedTest
    @MethodSource("validAndInvalidHashMapIds")
    public void willNotifyListenersWhenMilestoneRemoved(HashMap<Long, FakeTimestampedValue> initialMilestones,
                                                        Long potentiallyInvalidMilestoneId,
                                                        boolean idIsValid) {
        initialGoalData_ = initialGoalData_.withTargetMilestonesById(initialMilestones);
        resetPatient();
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        try {
            patient_.removeTargetMilestone(potentiallyInvalidMilestoneId);
        } catch (InvalidQueryException ignore) {
        }

        verify(listenerList_[0], times(idIsValid ? 1 : 0)).targetMilestoneRemoved(potentiallyInvalidMilestoneId);
        verify(listenerList_[1], times(idIsValid ? 1 : 0)).targetMilestoneRemoved(potentiallyInvalidMilestoneId);
    }

    @ParameterizedTest
    @MethodSource("validAndInvalidHashMapIds")
    public void willNotifyListenersWhenRecordChanged(HashMap<Long, FakeTimestampedValue> initialRecords, Long potentiallyInvalidRecordId,
                                                     boolean idIsValid) {
        initialGoalData_ = initialGoalData_.withRecordsById(initialRecords);
        resetPatient();
        FakeTimestampedValue updatedRecord = new RandomFakeTimestampedValue().generate();
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        try {
            patient_.editRecord(potentiallyInvalidRecordId, updatedRecord);
        } catch (InvalidQueryException ignore) {
        }

        verify(listenerList_[0], times(idIsValid ? 1 : 0)).recordChanged(potentiallyInvalidRecordId, updatedRecord);
        verify(listenerList_[1], times(idIsValid ? 1 : 0)).recordChanged(potentiallyInvalidRecordId, updatedRecord);
    }

    @ParameterizedTest
    @MethodSource("validAndInvalidHashMapIds")
    public void willNotifyListenersWhenMilestoneChanged(HashMap<Long, FakeTimestampedValue> initialMilestones,
                                                        Long potentiallyInvalidMilestoneId,
                                                        boolean idIsValid) {
        initialGoalData_ = initialGoalData_.withTargetMilestonesById(initialMilestones);
        resetPatient();
        FakeTimestampedValue updatedMilestone = new RandomFakeTimestampedValue().generate();
        patient_.registerListener(listenerList_[0]);
        patient_.registerListener(listenerList_[1]);

        try {
            patient_.editTargetMilestone(potentiallyInvalidMilestoneId, updatedMilestone);
        } catch (InvalidQueryException ignore) {
        }

        verify(listenerList_[0], times(idIsValid ? 1 : 0)).targetMilestoneChanged(potentiallyInvalidMilestoneId, updatedMilestone);
        verify(listenerList_[1], times(idIsValid ? 1 : 0)).targetMilestoneChanged(potentiallyInvalidMilestoneId, updatedMilestone);
    }

    // returns (HashMap<Long,FakeTimestampedValue> map, Long id, boolean idIsValid)
    private static Stream<Arguments> validAndInvalidHashMapIds() {
        HashMap<Long, FakeTimestampedValue> initialEntries =
                new RandomHashMap<>(new RandomLong(), new RandomFakeTimestampedValue()).generate();
        Long validId = pickRandomEntryIdFromMap(initialEntries);
        Long invalidId = pickRandomEntryIdNotInMap(initialEntries);

        return Stream.of(
                Arguments.of(initialEntries, validId, true),
                Arguments.of(initialEntries, invalidId, false)
        );
    }

    // returns (HashMap<Long, FakeTimestampedValue> initialMap, int numberOfEntriesToAdd, Set<Long> expectedIds)
    private static Stream<Arguments> idOrderingTestData() {
        RandomFakeTimestampedValue valueGenerator = new RandomFakeTimestampedValue();

        // case 1: adding two elements to empty hashmap
        HashMap<Long, FakeTimestampedValue> initialMap_case1 = new HashMap<>();
        int entriesToAdd_case1 = 2;
        Long[] indexZeroAndOne = {0L, 1L};
        HashSet<Long> expectedIds_case1 = new HashSet<>(Arrays.asList(indexZeroAndOne));

        // case 2: adding two elements to populated hashmap
        HashMap<Long, FakeTimestampedValue> initialMap_case2 = new HashMap<>();
        initialMap_case2.put(0L, valueGenerator.generate());
        initialMap_case2.put(1L, valueGenerator.generate());
        int entriesToAdd_case2 = 2;
        Long[] indexZeroOneTwoThree = {0L, 1L, 2L, 3L};
        HashSet<Long> expectedIds_case2 = new HashSet<>(Arrays.asList(indexZeroOneTwoThree));

        // case 3: adding one element to out-of-order hashmap
        HashMap<Long, FakeTimestampedValue> initialMap_case3 = new HashMap<>();
        initialMap_case3.put(0L, valueGenerator.generate());
        initialMap_case3.put(4L, valueGenerator.generate());
        int entriesToAdd_case3 = 1;
        Long[] indexZeroFourFive = {0L, 4L, 5L};
        HashSet<Long> expectedIds_case3 = new HashSet<>(Arrays.asList(indexZeroFourFive));

        // case 4: adding one element to hashmap with negative indices
        HashMap<Long, FakeTimestampedValue> initialMap_case4 = new HashMap<>();
        initialMap_case4.put(-1L, valueGenerator.generate());
        initialMap_case4.put(0L, valueGenerator.generate());
        int entriesToAdd_case4 = 1;
        Long[] indexNegativeOneZeroOne = {-1L, 0L, 1L};
        HashSet<Long> expectedIds_case4 = new HashSet<>(Arrays.asList(indexNegativeOneZeroOne));

        return Stream.of(
                Arguments.of(initialMap_case1, entriesToAdd_case1, expectedIds_case1),
                Arguments.of(initialMap_case2, entriesToAdd_case2, expectedIds_case2),
                Arguments.of(initialMap_case3, entriesToAdd_case3, expectedIds_case3),
                Arguments.of(initialMap_case4, entriesToAdd_case4, expectedIds_case4)
        );
    }

    // assumes non-empty map
    private static Long pickRandomEntryIdFromMap(HashMap<Long, FakeTimestampedValue> map) {
        ArrayList<Long> idList = new ArrayList<>(map.keySet());
        int indexOfSelectedId = new RandomInt().generate(0, idList.size() - 1);
        return idList.get(indexOfSelectedId);
    }

    private static Long pickRandomEntryIdNotInMap(HashMap<Long, FakeTimestampedValue> map) {
        ArrayList<Long> idList = new ArrayList<>(map.keySet());
        return new RandomOther<>(new RandomLong()).otherThan(idList);
    }
}