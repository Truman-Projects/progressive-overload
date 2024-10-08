package truman.progressiveoverload.goalManagement.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import truman.progressiveoverload.randomUtilities.RandomEnum;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomOther;
import truman.progressiveoverload.randomUtilities.RandomString;

class TestGoalData {
    private static final RandomLong longGenerator = new RandomLong();
    private static final RandomTimestampedValue<Long> timestampedLongGenerator = new RandomTimestampedValue<>(longGenerator);
    private static final RandomGoalData<Long> goalDataGenerator_ = new RandomGoalData<>(longGenerator);
    private static final RandomHashMap<Long, TimestampedValue<Long>> timestampedValueHashMapGenerator_ =
            new RandomHashMap<>(longGenerator, timestampedLongGenerator);


    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateBasicConstructor(String randomName, String randomDescription, GoalType randomGoalType) {
        HashMap<Long, TimestampedValue<Long>> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> randomTargetMilestones = timestampedValueHashMapGenerator_.generate();

        GoalData<Long> patient = new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords, randomTargetMilestones);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, randomRecords, randomTargetMilestones, patient);
    }

    @Test
    public void basicConstructorStoresInputsByCopy() {
        RandomOther<Long> uniqueKeyGenerator = new RandomOther<>(new RandomLong());
        HashMap<Long, TimestampedValue<Long>> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> randomTargetMilestones = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> originalRecords = new HashMap<>(randomRecords);
        HashMap<Long, TimestampedValue<Long>> originalTargetMilestones = new HashMap<>(randomTargetMilestones);
        // arbitrary keys and values to attempt to alter patient's records and targetMilestones by reference
        TimestampedValue<Long> randomTimestampedValue1 = timestampedLongGenerator.generate();
        Long randomKey1 = uniqueKeyGenerator.otherThan(new ArrayList<>(randomRecords.keySet()));
        TimestampedValue<Long> randomTimestampedValue2 = timestampedLongGenerator.generate();
        Long randomKey2 = uniqueKeyGenerator.otherThan(new ArrayList<>(randomTargetMilestones.keySet()));

        GoalData<Long> patient = new GoalData<>(new RandomString().generate(), new RandomString().generate(),
                new RandomEnum<>(GoalType.class).generate(), randomRecords, randomTargetMilestones);
        randomRecords.put(randomKey1, randomTimestampedValue1);
        randomTargetMilestones.put(randomKey2, randomTimestampedValue2);

        assertEquals(originalRecords, patient.recordsById());
        assertEquals(originalTargetMilestones, patient.targetMilestonesById());
    }

    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateConstructorOverloadWithoutTargetMilestones(String randomName, String randomDescription, GoalType randomGoalType) {
        HashMap<Long, TimestampedValue<Long>> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> emptyTargetMilestones = new HashMap<>();

        GoalData<Long> patient = new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, randomRecords, emptyTargetMilestones, patient);
    }

    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateConstructorOverloadWithoutTargetMilestonesOrRecords(String randomName, String randomDescription,
                                                                            GoalType randomGoalType) {
        HashMap<Long, TimestampedValue<Long>> emptyRecords = new HashMap<>();
        HashMap<Long, TimestampedValue<Long>> emptyTargetMilestones = new HashMap<>();

        GoalData<Long> patient = new GoalData<>(randomName, randomDescription, randomGoalType);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, emptyRecords, emptyTargetMilestones, patient);
    }

    @Test
    public void withNameWillReturnCopyWithNewName() {
        GoalData<Long> originalGoalData = goalDataGenerator_.generate();
        String newName = new RandomString().generate();

        GoalData<Long> goalDataWithNewName = originalGoalData.withName(newName);

        assertNotSame(originalGoalData, goalDataWithNewName);
        validateAllGoalFields(newName, originalGoalData.description(), originalGoalData.goalType(), originalGoalData.recordsById(),
                originalGoalData.targetMilestonesById(),
                goalDataWithNewName);
    }

    @Test
    public void withDescriptionWillReturnCopyWithNewDescription() {
        GoalData<Long> originalGoalData = goalDataGenerator_.generate();
        String newDescription = new RandomString().generate();

        GoalData<Long> goalDataWithNewDescription = originalGoalData.withDescription(newDescription);

        assertNotSame(originalGoalData, goalDataWithNewDescription);
        validateAllGoalFields(originalGoalData.name(), newDescription, originalGoalData.goalType(), originalGoalData.recordsById(),
                originalGoalData.targetMilestonesById(),
                goalDataWithNewDescription);
    }

    @ParameterizedTest
    @MethodSource("withGoalTypeWillReturnCopyWithNewGoalType_data")
    public void withGoalTypeWillReturnCopyWithNewGoalType_data(GoalType originalGoalType, GoalType newGoalType) {
        GoalData<Long> originalGoalData = goalDataGenerator_.generate().withGoalType(originalGoalType);

        GoalData<Long> goalDataWithNewGoalType = originalGoalData.withGoalType(newGoalType);

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
        GoalData<Long> originalGoalData = goalDataGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> newRecords = timestampedValueHashMapGenerator_.generate();

        GoalData<Long> goalDataWithNewRecords = originalGoalData.withRecordsById(newRecords);

        assertNotSame(originalGoalData, goalDataWithNewRecords);
        validateAllGoalFields(originalGoalData.name(), originalGoalData.description(), originalGoalData.goalType(), newRecords,
                originalGoalData.targetMilestonesById(),
                goalDataWithNewRecords);
    }

    @Test
    public void withTargetMilestonesByIdWillReturnCopyWithNewTargetMilestones() {
        GoalData<Long> originalGoalData = goalDataGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> newTargetMilestones = timestampedValueHashMapGenerator_.generate();

        GoalData<Long> goalDataWithNewTargetMilestones = originalGoalData.withTargetMilestonesById(newTargetMilestones);

        assertNotSame(originalGoalData, goalDataWithNewTargetMilestones);
        validateAllGoalFields(originalGoalData.name(), originalGoalData.description(), originalGoalData.goalType(),
                originalGoalData.recordsById(),
                newTargetMilestones,
                goalDataWithNewTargetMilestones);
    }

    @Test
    public void willReturnRecordsByCopy() {
        RandomOther<Long> uniqueKeyGenerator = new RandomOther<>(new RandomLong());
        GoalData<Long> patient = goalDataGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> originalRecords = new HashMap<>(patient.recordsById());
        // random key value pair to attempt to alter records received from getter
        Long randomKey = uniqueKeyGenerator.otherThan(new ArrayList<>(patient.recordsById().keySet()));
        TimestampedValue<Long> randomTimestampedValue = timestampedLongGenerator.generate();

        HashMap<Long, TimestampedValue<Long>> recordsFromPatient = patient.recordsById();
        recordsFromPatient.put(randomKey, randomTimestampedValue);

        assertEquals(originalRecords, patient.recordsById());
    }

    @Test
    public void willReturnTargetMilestonesByCopy() {
        RandomOther<Long> uniqueKeyGenerator = new RandomOther<>(new RandomLong());
        GoalData<Long> patient = goalDataGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> originalTargetMilestones = new HashMap<>(patient.targetMilestonesById());
        // random key value pair to attempt to alter targetMilestones received from getter
        Long randomKey = uniqueKeyGenerator.otherThan(new ArrayList<>(patient.recordsById().keySet()));
        TimestampedValue<Long> randomTimestampedValue = timestampedLongGenerator.generate();

        HashMap<Long, TimestampedValue<Long>> targetMilestonesFromPatient = patient.targetMilestonesById();
        targetMilestonesFromPatient.put(randomKey, randomTimestampedValue);

        assertEquals(originalTargetMilestones, patient.targetMilestonesById());
    }

    @ParameterizedTest
    @MethodSource("testEqualityOperator_data")
    public void testEqualityOperator(GoalData<Long> goalData1, GoalData<Long> goalData2,
                                     boolean expectedEquality) {
        assertEquals(expectedEquality, goalData1.equals(goalData2));
    }

    // returns (GoalData<Long> goalData1, GoalData<Long> goalData2, boolean expectedEquals)
    private static Stream<Arguments> testEqualityOperator_data() {
        RandomString stringGen = new RandomString();
        RandomOther<String> uniqueStringGen = new RandomOther<>(stringGen);
        String name1 = stringGen.generate();
        String name2 = uniqueStringGen.otherThan(name1);
        String description1 = stringGen.generate();
        String description2 = uniqueStringGen.otherThan(description1);
        GoalType goalType1 = GoalType.MINIMIZE;
        GoalType goalType2 = GoalType.MAXIMIZE;
        HashMap<Long, TimestampedValue<Long>> records1 = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> records2 = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> targetMilestones1 = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedValue<Long>> targetMilestones2 = timestampedValueHashMapGenerator_.generate();

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
        GoalData<Long> randomGoalData = goalDataGenerator_.generate();
        GoalData<Duration> durationBasedGoalData = new GoalData<>(randomGoalData.name(), randomGoalData.description(),
                randomGoalData.goalType());

        assertNotEquals(randomGoalData, durationBasedGoalData);
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


    private <GoalFlavour> void validateAllGoalFields(String expectedName,
                                                     String expectedDescription,
                                                     GoalType expectedGoalType,
                                                     HashMap<Long, TimestampedValue<GoalFlavour>> expectedRecords,
                                                     HashMap<Long, TimestampedValue<GoalFlavour>> expectedTargetMilestones,
                                                     GoalData<GoalFlavour> actualGoalData) {
        assertEquals(expectedName, actualGoalData.name());
        assertEquals(expectedDescription, actualGoalData.description());
        assertEquals(expectedGoalType, actualGoalData.goalType());
        assertEquals(expectedRecords, actualGoalData.recordsById());
        assertEquals(expectedTargetMilestones, actualGoalData.targetMilestonesById());
    }
}