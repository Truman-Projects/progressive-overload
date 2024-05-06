package truman.progressiveoverload.goalManagement.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.measurement.I_TimestampedValue;
import truman.progressiveoverload.measurement.RandomFakeTimestampedValue;
import truman.progressiveoverload.measurement.TimestampedMass;
import truman.progressiveoverload.randomUtilities.RandomEnum;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomOther;
import truman.progressiveoverload.randomUtilities.RandomString;

class TestGoalData {
    private static final RandomFakeTimestampedValue fakeTimestampedValueGenerator_ = new RandomFakeTimestampedValue();
    private static final RandomHashMap<Long, FakeTimestampedValue> timestampedValueHashMapGenerator_ =
            new RandomHashMap<>(new RandomLong(), fakeTimestampedValueGenerator_);


    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateBasicConstructor(String randomName, String randomDescription, GoalType randomGoalType) {
        HashMap<Long, FakeTimestampedValue> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, FakeTimestampedValue> randomTargetMilestones = timestampedValueHashMapGenerator_.generate();

        GoalData<FakeTimestampedValue> patient = new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords,
                randomTargetMilestones);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, randomRecords, randomTargetMilestones, patient);
    }

    @Test
    public void basicConstructorStoresInputsByCopy() {
        RandomOther<Long> uniqueKeyGenerator = new RandomOther<>(new RandomLong());
        HashMap<Long, FakeTimestampedValue> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, FakeTimestampedValue> randomTargetMilestones = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, FakeTimestampedValue> originalRecords = new HashMap<>(randomRecords);
        HashMap<Long, FakeTimestampedValue> originalTargetMilestones = new HashMap<>(randomTargetMilestones);
        // arbitrary keys and values to attempt to alter patient's records and targetMilestones by reference
        FakeTimestampedValue randomTimestampedValue1 = fakeTimestampedValueGenerator_.generate();
        Long randomKey1 = uniqueKeyGenerator.otherThan(new ArrayList<>(randomRecords.keySet()));
        FakeTimestampedValue randomTimestampedValue2 = fakeTimestampedValueGenerator_.generate();
        Long randomKey2 = uniqueKeyGenerator.otherThan(new ArrayList<>(randomTargetMilestones.keySet()));

        GoalData<FakeTimestampedValue> patient = new GoalData<>(new RandomString().generate(), new RandomString().generate(),
                new RandomEnum<>(GoalType.class).generate(), randomRecords, randomTargetMilestones);
        randomRecords.put(randomKey1, randomTimestampedValue1);
        randomTargetMilestones.put(randomKey2, randomTimestampedValue2);

        assertEquals(originalRecords, patient.recordsById());
        assertEquals(originalTargetMilestones, patient.targetMilestonesById());
    }

    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateConstructorOverloadWithoutTargetMilestones(String randomName, String randomDescription, GoalType randomGoalType) {
        HashMap<Long, FakeTimestampedValue> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, FakeTimestampedValue> emptyTargetMilestones = new HashMap<>();

        GoalData<FakeTimestampedValue> patient = new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, randomRecords, emptyTargetMilestones, patient);
    }

    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateConstructorOverloadWithoutTargetMilestonesOrRecords(String randomName, String randomDescription,
                                                                            GoalType randomGoalType) {
        HashMap<Long, FakeTimestampedValue> emptyRecords = new HashMap<>();
        HashMap<Long, FakeTimestampedValue> emptyTargetMilestones = new HashMap<>();

        GoalData<FakeTimestampedValue> patient = new GoalData<>(randomName, randomDescription, randomGoalType);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, emptyRecords, emptyTargetMilestones, patient);
    }

    @Test
    public void withDescriptionWillReturnCopyWithNewDescription() {
        GoalData<FakeTimestampedValue> originalGoalData = new RandomGoalData().generate();
        String newDescription = new RandomString().generate();

        GoalData<FakeTimestampedValue> goalDataWithNewDescription = originalGoalData.withDescription(newDescription);

        assertNotSame(originalGoalData, goalDataWithNewDescription);
        validateAllGoalFields(originalGoalData.name(), newDescription, originalGoalData.goalType(), originalGoalData.recordsById(),
                originalGoalData.targetMilestonesById(),
                goalDataWithNewDescription);
    }

    @ParameterizedTest
    @MethodSource("withGoalTypeWillReturnCopyWithNewGoalType_data")
    public void withGoalTypeWillReturnCopyWithNewGoalType_data(GoalType originalGoalType, GoalType newGoalType) {
        GoalData<FakeTimestampedValue> originalGoalData = new RandomGoalData().generate().withGoalType(originalGoalType);

        GoalData<FakeTimestampedValue> goalDataWithNewGoalType = originalGoalData.withGoalType(newGoalType);

        assertNotSame(originalGoalData, goalDataWithNewGoalType);
        validateAllGoalFields(originalGoalData.name(), originalGoalData.description(), newGoalType, originalGoalData.recordsById(),
                originalGoalData.targetMilestonesById(),
                goalDataWithNewGoalType);
    }

    // Returns (GoalType originalGoalType, GoalType newGoalType)
    private static Stream<Arguments> withGoalTypeWillReturnCopyWithNewGoalType_data() {
        return Stream.of(
                Arguments.of(GoalType.MINIMIZE, GoalType.MINIMIZE),
                Arguments.of(GoalType.MINIMIZE, GoalType.MAXIMIZE),
                Arguments.of(GoalType.MAXIMIZE, GoalType.MINIMIZE),
                Arguments.of(GoalType.MAXIMIZE, GoalType.MAXIMIZE)
        );
    }

    @Test
    public void withRecordsByIdWillReturnCopyWithNewRecords() {
        GoalData<FakeTimestampedValue> originalGoalData = new RandomGoalData().generate();
        HashMap<Long, FakeTimestampedValue> newRecords = timestampedValueHashMapGenerator_.generate();

        GoalData<FakeTimestampedValue> goalDataWithNewRecords = originalGoalData.withRecordsById(newRecords);

        assertNotSame(originalGoalData, goalDataWithNewRecords);
        validateAllGoalFields(originalGoalData.name(), originalGoalData.description(), originalGoalData.goalType(), newRecords,
                originalGoalData.targetMilestonesById(),
                goalDataWithNewRecords);
    }

    @Test
    public void withTargetMilestonesByIdWillReturnCopyWithNewTargetMilestones() {
        GoalData<FakeTimestampedValue> originalGoalData = new RandomGoalData().generate();
        HashMap<Long, FakeTimestampedValue> newTargetMilestones = timestampedValueHashMapGenerator_.generate();

        GoalData<FakeTimestampedValue> goalDataWithNewTargetMilestones = originalGoalData.withTargetMilestonesById(newTargetMilestones);

        assertNotSame(originalGoalData, goalDataWithNewTargetMilestones);
        validateAllGoalFields(originalGoalData.name(), originalGoalData.description(), originalGoalData.goalType(),
                originalGoalData.recordsById(),
                newTargetMilestones,
                goalDataWithNewTargetMilestones);
    }

    @Test
    public void willReturnRecordsByCopy() {
        RandomOther<Long> uniqueKeyGenerator = new RandomOther<>(new RandomLong());
        GoalData<FakeTimestampedValue> patient = new RandomGoalData().generate();
        HashMap<Long, FakeTimestampedValue> originalRecords = new HashMap<>(patient.recordsById());
        // random key value pair to attempt to alter records received from getter
        Long randomKey = uniqueKeyGenerator.otherThan(new ArrayList<>(patient.recordsById().keySet()));
        FakeTimestampedValue randomTimestampedValue = fakeTimestampedValueGenerator_.generate();

        HashMap<Long, FakeTimestampedValue> recordsFromPatient = patient.recordsById();
        recordsFromPatient.put(randomKey, randomTimestampedValue);

        assertEquals(originalRecords, patient.recordsById());
    }

    @Test
    public void willReturnTargetMilestonesByCopy() {
        RandomOther<Long> uniqueKeyGenerator = new RandomOther<>(new RandomLong());
        GoalData<FakeTimestampedValue> patient = new RandomGoalData().generate();
        HashMap<Long, FakeTimestampedValue> originalTargetMilestones = new HashMap<>(patient.targetMilestonesById());
        // random key value pair to attempt to alter targetMilestones received from getter
        Long randomKey = uniqueKeyGenerator.otherThan(new ArrayList<>(patient.recordsById().keySet()));
        FakeTimestampedValue randomTimestampedValue = fakeTimestampedValueGenerator_.generate();

        HashMap<Long, FakeTimestampedValue> targetMilestonesFromPatient = patient.targetMilestonesById();
        targetMilestonesFromPatient.put(randomKey, randomTimestampedValue);

        assertEquals(originalTargetMilestones, patient.targetMilestonesById());
    }

    @ParameterizedTest
    @MethodSource("testEqualityOperator_data")
    public void testEqualityOperator(GoalData<FakeTimestampedValue> goalData1, GoalData<FakeTimestampedValue> goalData2,
                                     boolean expectedEquality) {
        assertEquals(expectedEquality, goalData1.equals(goalData2));
    }

    // returns (GoalData<FakeTimestampedValue goalData1, GoalData<FakeTimestampedValue goalData2, boolean expectedEquals)
    private static Stream<Arguments> testEqualityOperator_data() {
        RandomString stringGen = new RandomString();
        RandomOther<String> uniqueStringGen = new RandomOther<>(stringGen);
        String name1 = stringGen.generate();
        String name2 = uniqueStringGen.otherThan(name1);
        String description1 = stringGen.generate();
        String description2 = uniqueStringGen.otherThan(description1);
        GoalType goalType1 = GoalType.MINIMIZE;
        GoalType goalType2 = GoalType.MAXIMIZE;
        HashMap<Long, FakeTimestampedValue> records1 = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, FakeTimestampedValue> records2 = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, FakeTimestampedValue> targetMilestones1 = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, FakeTimestampedValue> targetMilestones2 = timestampedValueHashMapGenerator_.generate();

        return Stream.of(
                // equals
                Arguments.of(new GoalData<>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<>(name1, description1, goalType1, records1, targetMilestones1), true),
                // different names
                Arguments.of(new GoalData<>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<>(name2, description1, goalType1, records1, targetMilestones1), false),
                // different descriptions
                Arguments.of(new GoalData<>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<>(name1, description2, goalType1, records1, targetMilestones1), false),
                // different goalTypes
                Arguments.of(new GoalData<>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<>(name1, description1, goalType2, records1, targetMilestones1), false),
                // different records
                Arguments.of(new GoalData<>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<>(name1, description1, goalType1, records2, targetMilestones1), false),
                // different targetMilestones
                Arguments.of(new GoalData<>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<>(name1, description1, goalType1, records1, targetMilestones2), false)
        );


    }

    @Test
    public void canCompareGoalDataWithDifferentTypes() {
        GoalData<FakeTimestampedValue> randomGoalData = new RandomGoalData().generate();
        GoalData<TimestampedMass> massBasedGoalData = new GoalData<>(randomGoalData.name(), randomGoalData.description(),
                randomGoalData.goalType());

        assertNotEquals(randomGoalData, massBasedGoalData);
    }


    // returns (String randomName, String randomDescription, GoalType randomGoalType)
    private static Stream<Arguments> basicGoalDataFields() {
        String randomName = new RandomString().generate();
        String randomDescription = new RandomString().generate();

        return Stream.of(
                Arguments.of(randomName, randomDescription, GoalType.MAXIMIZE),
                Arguments.of(randomName, randomDescription, GoalType.MINIMIZE)
        );
    }


    private <TimestampedType extends I_TimestampedValue> void validateAllGoalFields(String expectedName,
                                                                                    String expectedDescription,
                                                                                    GoalType expectedGoalType,
                                                                                    HashMap<Long, TimestampedType> expectedRecords,
                                                                                    HashMap<Long, TimestampedType> expectedTargetMilestones,
                                                                                    GoalData<TimestampedType> actualGoalData) {
        assertEquals(expectedName, actualGoalData.name());
        assertEquals(expectedDescription, actualGoalData.description());
        assertEquals(expectedGoalType, actualGoalData.goalType());
        assertEquals(expectedRecords, actualGoalData.recordsById());
        assertEquals(expectedTargetMilestones, actualGoalData.targetMilestonesById());
    }
}