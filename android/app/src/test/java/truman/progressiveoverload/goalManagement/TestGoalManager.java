package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import truman.progressiveoverload.goalManagement.api.I_GoalUpdateListener;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.measurement.RandomFakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomInt;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomOther;
import truman.progressiveoverload.randomUtilities.RandomString;

class TestGoalManager {
    private static final RandomGoalData goalDataGen_ = new RandomGoalData();

    // intermediate interface to appease the mockito gods
    private interface I_FakeValueGoalUpdateListener extends I_GoalUpdateListener<FakeTimestampedValue> {
        @Override
        void goalDescriptionChanged(String newDescription);

        @Override
        void goalTypeChanged(GoalType newGoalType);

        @Override
        void recordAdded(Long recordId, FakeTimestampedValue record);

        @Override
        void recordRemoved(Long recordId);

        @Override
        void recordChanged(Long recordId, FakeTimestampedValue updatedRecord);

        @Override
        void targetMilestoneAdded(Long milestoneId, FakeTimestampedValue targetMilestone);

        @Override
        void targetMilestoneRemoved(Long milestoneId);

        @Override
        void targetMilestoneChanged(Long recordId, FakeTimestampedValue updatedRecord);

    }

    @Test
    public void willProvideCorrectCurrentStateOnConstruction() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();

        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);

        assertEquals(initialGoalData, patient.currentState());
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterChangingDescription() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        String newDescription = new RandomOther<>(new RandomString()).otherThan(initialGoalData.description());
        GoalData<FakeTimestampedValue> goalDataWithNewDescription = initialGoalData.withDescription(newDescription);

        patient.changeGoalDescription(newDescription);

        assertEquals(goalDataWithNewDescription, patient.currentState());
    }

    @ParameterizedTest
    @MethodSource("willProvideUpdatedCurrentStateAfterChangingGoalType_data")
    public void willProvideUpdatedCurrentStateAfterChangingGoalType(GoalType initialGoalType, GoalType newGoalType) {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withGoalType(initialGoalType);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        GoalData<FakeTimestampedValue> goalDataWithNewGoalType = initialGoalData.withGoalType(newGoalType);

        patient.changeGoalType(newGoalType);

        assertEquals(goalDataWithNewGoalType, patient.currentState());
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
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        FakeTimestampedValue newRecord = new RandomFakeTimestampedValue().generate();
        HashMap<Long, FakeTimestampedValue> initialRecords = initialGoalData.recordsById();

        Long newRecordId = patient.addRecord(newRecord);

        HashMap<Long, FakeTimestampedValue> expectedRecords = new HashMap<>(initialRecords);
        expectedRecords.put(newRecordId, newRecord);
        GoalData<FakeTimestampedValue> goalDataWithExpectedRecords = initialGoalData.withRecordsById(expectedRecords);
        assertEquals(goalDataWithExpectedRecords, patient.currentState());
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterAddingMilestone() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        FakeTimestampedValue newMilestone = new RandomFakeTimestampedValue().generate();
        HashMap<Long, FakeTimestampedValue> initialMilestones = initialGoalData.targetMilestonesById();

        Long newMilestoneId = patient.addTargetMilestone(newMilestone);

        HashMap<Long, FakeTimestampedValue> expectedMilestones = new HashMap<>(initialMilestones);
        expectedMilestones.put(newMilestoneId, newMilestone);
        GoalData<FakeTimestampedValue> goalDataWithExpectedMilestones = initialGoalData.withTargetMilestonesById(expectedMilestones);
        assertEquals(goalDataWithExpectedMilestones, patient.currentState());
    }

    @ParameterizedTest
    @MethodSource("idOrderingTestData")
    public void willGenerateRecordIdsInIncreasingOrder(HashMap<Long, FakeTimestampedValue> initialMap, int numberOfEntriesToAdd,
                                                       Set<Long> expectedIds) {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withRecordsById(initialMap);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        RandomFakeTimestampedValue valueGenerator = new RandomFakeTimestampedValue();

        for (int recordsAdded = 0; recordsAdded < numberOfEntriesToAdd; recordsAdded++) {
            FakeTimestampedValue randomValue = valueGenerator.generate();
            patient.addRecord(randomValue);
        }

        Set<Long> actualIds = patient.currentState().recordsById().keySet();
        assertEquals(expectedIds, actualIds);
    }

    @ParameterizedTest
    @MethodSource("idOrderingTestData")
    public void willGenerateMilestoneIdsInIncreasingOrder(HashMap<Long, FakeTimestampedValue> initialMap, int numberOfEntriesToAdd,
                                                          Set<Long> expectedIds) {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withTargetMilestonesById(initialMap);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        RandomFakeTimestampedValue valueGenerator = new RandomFakeTimestampedValue();

        for (int milestonesAdded = 0; milestonesAdded < numberOfEntriesToAdd; milestonesAdded++) {
            FakeTimestampedValue randomValue = valueGenerator.generate();
            patient.addTargetMilestone(randomValue);
        }

        Set<Long> actualIds = patient.currentState().targetMilestonesById().keySet();
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterRemovingValidRecord() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        HashMap<Long, FakeTimestampedValue> initialRecords = initialGoalData.recordsById();
        Long recordIdToRemove = pickRandomEntryIdFromMap(initialRecords);

        patient.removeRecord(recordIdToRemove);

        HashMap<Long, FakeTimestampedValue> expectedRecords = new HashMap<>(initialRecords);
        expectedRecords.remove(recordIdToRemove);
        GoalData<FakeTimestampedValue> goalDataWithExpectedRecords = initialGoalData.withRecordsById(expectedRecords);
        assertEquals(goalDataWithExpectedRecords, patient.currentState());
    }

    @Test
    public void willProvideUpdatedCurrentStateAfterRemovingValidMilestone() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        HashMap<Long, FakeTimestampedValue> initialMilestones = initialGoalData.targetMilestonesById();
        Long milestoneIdToRemove = pickRandomEntryIdFromMap(initialMilestones);

        patient.removeTargetMilestone(milestoneIdToRemove);

        HashMap<Long, FakeTimestampedValue> expectedMilestones = new HashMap<>(initialMilestones);
        expectedMilestones.remove(milestoneIdToRemove);
        GoalData<FakeTimestampedValue> goalDataWithExpectedMilestones = initialGoalData.withTargetMilestonesById(expectedMilestones);
        assertEquals(goalDataWithExpectedMilestones, patient.currentState());
    }

    @Test
    public void willThrowExceptionWhenAttemptingToRemoveInvalidRecordId() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        Long invalidRecordId = pickRandomEntryIdNotInMap(initialGoalData.recordsById());

        assertThrows(IndexOutOfBoundsException.class, () -> patient.removeRecord(invalidRecordId));
    }

    @Test
    public void willThrowExceptionWhenAttemptingToRemoveInvalidMilestoneId() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        Long invalidMilestoneId = pickRandomEntryIdNotInMap(initialGoalData.recordsById());

        assertThrows(IndexOutOfBoundsException.class, () -> patient.removeTargetMilestone(invalidMilestoneId));
    }

    @Test
    public void willNotReuseRemovedRecordIds() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withRecordsById(new HashMap<>());
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        RandomFakeTimestampedValue valueGen = new RandomFakeTimestampedValue();
        Long firstRecordId = patient.addRecord(valueGen.generate());
        patient.removeRecord(firstRecordId);

        Long secondRecordId = patient.addRecord(valueGen.generate());

        assertNotEquals(firstRecordId, secondRecordId);
    }

    @Test
    public void willNotReuseRemovedMilestoneIds() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withTargetMilestonesById(new HashMap<>());
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        RandomFakeTimestampedValue valueGen = new RandomFakeTimestampedValue();
        Long firstMilestoneId = patient.addTargetMilestone(valueGen.generate());
        patient.removeTargetMilestone(firstMilestoneId);

        Long secondMilestoneId = patient.addTargetMilestone(valueGen.generate());

        assertNotEquals(firstMilestoneId, secondMilestoneId);
    }

    @Test
    public void willProvideCorrectCurrentStateAfterEditingRecord() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        HashMap<Long, FakeTimestampedValue> initialRecords = initialGoalData.recordsById();
        Long recordIdToEdit = pickRandomEntryIdFromMap(initialRecords);
        FakeTimestampedValue recordAfterEditing =
                new RandomOther<>(new RandomFakeTimestampedValue()).otherThan(new ArrayList<>(initialRecords.values()));

        patient.editRecord(recordIdToEdit, recordAfterEditing);

        assertEquals(recordAfterEditing, patient.currentState().recordsById().get(recordIdToEdit));
    }

    @Test
    public void willProvideCorrectCurrentStateAfterEditingMilestone() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        HashMap<Long, FakeTimestampedValue> initialMilestones = initialGoalData.targetMilestonesById();
        Long milestoneIdToEdit = pickRandomEntryIdFromMap(initialMilestones);
        FakeTimestampedValue milestoneAfterEditing =
                new RandomOther<>(new RandomFakeTimestampedValue()).otherThan(new ArrayList<>(initialMilestones.values()));

        patient.editTargetMilestone(milestoneIdToEdit, milestoneAfterEditing);

        assertEquals(milestoneAfterEditing, patient.currentState().targetMilestonesById().get(milestoneIdToEdit));
    }

    @Test
    public void willThrowExceptionWhenAttemptingToEditInvalidRecordId() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        Long invalidRecordId = pickRandomEntryIdNotInMap(initialGoalData.recordsById());
        FakeTimestampedValue randomUpdatedRecord = new RandomFakeTimestampedValue().generate();

        assertThrows(IndexOutOfBoundsException.class, () -> patient.editRecord(invalidRecordId, randomUpdatedRecord));
    }

    @Test
    public void willThrowExceptionWhenAttemptingToEditInvalidMilestoneId() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        Long invalidMilestoneId = pickRandomEntryIdNotInMap(initialGoalData.targetMilestonesById());
        FakeTimestampedValue randomUpdatedMilestone = new RandomFakeTimestampedValue().generate();

        assertThrows(IndexOutOfBoundsException.class, () -> patient.editTargetMilestone(invalidMilestoneId, randomUpdatedMilestone));
    }

    @Test
    public void willDoNothingIfListenerRegisteredTwice() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        I_FakeValueGoalUpdateListener mockListener = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener);
        String updatedDescription = new RandomString().generate();

        // register the same listener again
        patient.registerListener(mockListener);
        // we are only testing changeGoalDescription() as it would be impractical to test this for every single changed signal
        patient.changeGoalDescription(updatedDescription);

        verify(mockListener, times(1)).goalDescriptionChanged(updatedDescription);
    }

    @Test
    public void willNotNotifyUnregisteredListener() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        I_FakeValueGoalUpdateListener mockListener = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener);
        String updatedDescription = new RandomString().generate();
        patient.changeGoalDescription(updatedDescription);

        patient.unregisterListener(mockListener);
        // we are only testing changeGoalDescription() as it would be impractical to test this for every single changed signal
        patient.changeGoalDescription(updatedDescription);

        verify(mockListener, times(1)).goalDescriptionChanged(updatedDescription);
    }

    @Test
    public void willDoNothingWhenUnregisteringUnknownListener() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        I_FakeValueGoalUpdateListener mockListener = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener);
        String updatedDescription = new RandomString().generate();
        I_FakeValueGoalUpdateListener unknownListener = mock(I_FakeValueGoalUpdateListener.class);


        patient.unregisterListener(unknownListener);
        // we are only testing changeGoalDescription() as it would be impractical to test this for every single changed signal
        patient.changeGoalDescription(updatedDescription);

        verify(mockListener, times(1)).goalDescriptionChanged(updatedDescription);
    }

    @ParameterizedTest
    @MethodSource("willNotifyListenersWhenDescriptionChanged_data")
    public void willNotifyListenersWhenDescriptionChanged(String initialDescription, String updatedDescription,
                                                          int timesDescriptionChanged) {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withDescription(initialDescription);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        I_FakeValueGoalUpdateListener mockListener1 = mock(I_FakeValueGoalUpdateListener.class);
        I_FakeValueGoalUpdateListener mockListener2 = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener1);
        patient.registerListener(mockListener2);

        patient.changeGoalDescription(updatedDescription);

        verify(mockListener1, times(timesDescriptionChanged)).goalDescriptionChanged(updatedDescription);
        verify(mockListener2, times(timesDescriptionChanged)).goalDescriptionChanged(updatedDescription);
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
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withGoalType(initialGoalType);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        I_FakeValueGoalUpdateListener mockListener1 = mock(I_FakeValueGoalUpdateListener.class);
        I_FakeValueGoalUpdateListener mockListener2 = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener1);
        patient.registerListener(mockListener2);

        patient.changeGoalType(updatedGoalType);

        verify(mockListener1, times(timesGoalTypeChanged)).goalTypeChanged(updatedGoalType);
        verify(mockListener2, times(timesGoalTypeChanged)).goalTypeChanged(updatedGoalType);
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
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        FakeTimestampedValue newRecord = new RandomFakeTimestampedValue().generate();
        I_FakeValueGoalUpdateListener mockListener1 = mock(I_FakeValueGoalUpdateListener.class);
        I_FakeValueGoalUpdateListener mockListener2 = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener1);
        patient.registerListener(mockListener2);

        Long newRecordId = patient.addRecord(newRecord);

        verify(mockListener1, times(1)).recordAdded(newRecordId, newRecord);
        verify(mockListener2, times(1)).recordAdded(newRecordId, newRecord);
    }

    @Test
    public void willNotifyListenersWhenMilestoneAdded() {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate();
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        FakeTimestampedValue newMilestone = new RandomFakeTimestampedValue().generate();
        I_FakeValueGoalUpdateListener mockListener1 = mock(I_FakeValueGoalUpdateListener.class);
        I_FakeValueGoalUpdateListener mockListener2 = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener1);
        patient.registerListener(mockListener2);

        Long newMilestoneId = patient.addTargetMilestone(newMilestone);

        verify(mockListener1, times(1)).targetMilestoneAdded(newMilestoneId, newMilestone);
        verify(mockListener2, times(1)).targetMilestoneAdded(newMilestoneId, newMilestone);
    }

    @ParameterizedTest
    @MethodSource("validAndInvalidHashMapIds")
    public void willNotifyListenersWhenRecordRemoved(HashMap<Long, FakeTimestampedValue> initialRecords, Long potentiallyInvalidRecordId,
                                                     boolean idIsValid) {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withRecordsById(initialRecords);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        I_FakeValueGoalUpdateListener mockListener1 = mock(I_FakeValueGoalUpdateListener.class);
        I_FakeValueGoalUpdateListener mockListener2 = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener1);
        patient.registerListener(mockListener2);

        try {
            patient.removeRecord(potentiallyInvalidRecordId);
        } catch (IndexOutOfBoundsException ignore) {
        }

        verify(mockListener1, times(idIsValid ? 1 : 0)).recordRemoved(potentiallyInvalidRecordId);
        verify(mockListener2, times(idIsValid ? 1 : 0)).recordRemoved(potentiallyInvalidRecordId);
    }

    @ParameterizedTest
    @MethodSource("validAndInvalidHashMapIds")
    public void willNotifyListenersWhenMilestoneRemoved(HashMap<Long, FakeTimestampedValue> initialMilestones,
                                                        Long potentiallyInvalidMilestoneId,
                                                        boolean idIsValid) {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withTargetMilestonesById(initialMilestones);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        I_FakeValueGoalUpdateListener mockListener1 = mock(I_FakeValueGoalUpdateListener.class);
        I_FakeValueGoalUpdateListener mockListener2 = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener1);
        patient.registerListener(mockListener2);

        try {
            patient.removeTargetMilestone(potentiallyInvalidMilestoneId);
        } catch (IndexOutOfBoundsException ignore) {
        }

        verify(mockListener1, times(idIsValid ? 1 : 0)).targetMilestoneRemoved(potentiallyInvalidMilestoneId);
        verify(mockListener2, times(idIsValid ? 1 : 0)).targetMilestoneRemoved(potentiallyInvalidMilestoneId);
    }

    @ParameterizedTest
    @MethodSource("validAndInvalidHashMapIds")
    public void willNotifyListenersWhenRecordChanged(HashMap<Long, FakeTimestampedValue> initialRecords, Long potentiallyInvalidRecordId,
                                                     boolean idIsValid) {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withRecordsById(initialRecords);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        FakeTimestampedValue updatedRecord = new RandomFakeTimestampedValue().generate();
        I_FakeValueGoalUpdateListener mockListener1 = mock(I_FakeValueGoalUpdateListener.class);
        I_FakeValueGoalUpdateListener mockListener2 = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener1);
        patient.registerListener(mockListener2);

        try {
            patient.editRecord(potentiallyInvalidRecordId, updatedRecord);
        } catch (IndexOutOfBoundsException ignore) {
        }

        verify(mockListener1, times(idIsValid ? 1 : 0)).recordChanged(potentiallyInvalidRecordId, updatedRecord);
        verify(mockListener2, times(idIsValid ? 1 : 0)).recordChanged(potentiallyInvalidRecordId, updatedRecord);
    }

    @ParameterizedTest
    @MethodSource("validAndInvalidHashMapIds")
    public void willNotifyListenersWhenMilestoneChanged(HashMap<Long, FakeTimestampedValue> initialMilestones,
                                                        Long potentiallyInvalidMilestoneId,
                                                        boolean idIsValid) {
        GoalData<FakeTimestampedValue> initialGoalData = goalDataGen_.generate().withTargetMilestonesById(initialMilestones);
        GoalManager<FakeTimestampedValue> patient = new GoalManager<>(initialGoalData);
        FakeTimestampedValue updatedMilestone = new RandomFakeTimestampedValue().generate();
        I_FakeValueGoalUpdateListener mockListener1 = mock(I_FakeValueGoalUpdateListener.class);
        I_FakeValueGoalUpdateListener mockListener2 = mock(I_FakeValueGoalUpdateListener.class);
        patient.registerListener(mockListener1);
        patient.registerListener(mockListener2);

        try {
            patient.editTargetMilestone(potentiallyInvalidMilestoneId, updatedMilestone);
        } catch (IndexOutOfBoundsException ignore) {
        }

        verify(mockListener1, times(idIsValid ? 1 : 0)).targetMilestoneChanged(potentiallyInvalidMilestoneId, updatedMilestone);
        verify(mockListener2, times(idIsValid ? 1 : 0)).targetMilestoneChanged(potentiallyInvalidMilestoneId, updatedMilestone);
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