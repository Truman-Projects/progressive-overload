package truman.progressiveoverload.goalManagement.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.measurement.I_TimestampedValue;
import truman.progressiveoverload.measurement.RandomFakeTimestampedValue;
import truman.progressiveoverload.measurement.TimestampedMass;
import truman.progressiveoverload.randomUtilities.RandomArrayList;
import truman.progressiveoverload.randomUtilities.RandomEnum;
import truman.progressiveoverload.randomUtilities.RandomString;

class TestGoalData {
    private static final RandomArrayList<FakeTimestampedValue> timestampedValueListGenerator_ =
            new RandomArrayList<>(new RandomFakeTimestampedValue());

    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateBasicConstructor(String randomName, String randomDescription, GoalType randomGoalType) {
        ArrayList<FakeTimestampedValue> randomRecords = timestampedValueListGenerator_.generate();
        ArrayList<FakeTimestampedValue> randomTargetMilestones = timestampedValueListGenerator_.generate();

        GoalData<FakeTimestampedValue> patient = new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords,
                randomTargetMilestones);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, randomRecords, randomTargetMilestones, patient);
    }

    @Test
    public void basicConstructorStoresInputsByCopy() {
        ArrayList<FakeTimestampedValue> randomRecords = timestampedValueListGenerator_.generate();
        ArrayList<FakeTimestampedValue> randomTargetMilestones = timestampedValueListGenerator_.generate();
        ArrayList<FakeTimestampedValue> originalRecords = new ArrayList<>(randomRecords);
        ArrayList<FakeTimestampedValue> originalTargetMilestones = new ArrayList<>(randomTargetMilestones);
        FakeTimestampedValue randomTimestampedValue1 = new RandomFakeTimestampedValue().generate();
        FakeTimestampedValue randomTimestampedValue2 = new RandomFakeTimestampedValue().generate();

        GoalData<FakeTimestampedValue> patient = new GoalData<>(new RandomString().generate(), new RandomString().generate(),
                new RandomEnum<>(GoalType.class).generate(), randomRecords, randomTargetMilestones);
        randomRecords.add(randomTimestampedValue1);
        randomTargetMilestones.add(randomTimestampedValue2);

        assertEquals(originalRecords, patient.records());
        assertEquals(originalTargetMilestones, patient.targetMilestones());
    }

    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateConstructorOverloadWithoutTargetMilestones(String randomName, String randomDescription, GoalType randomGoalType) {
        ArrayList<FakeTimestampedValue> randomRecords = timestampedValueListGenerator_.generate();
        ArrayList<FakeTimestampedValue> emptyTargetMilestones = new ArrayList<>();

        GoalData<FakeTimestampedValue> patient = new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, randomRecords, emptyTargetMilestones, patient);
    }

    @ParameterizedTest
    @MethodSource("basicGoalDataFields")
    public void validateConstructorOverloadWithoutTargetMilestonesOrRecords(String randomName, String randomDescription,
                                                                            GoalType randomGoalType) {
        ArrayList<FakeTimestampedValue> emptyRecords = new ArrayList<>();
        ArrayList<FakeTimestampedValue> emptyTargetMilestones = new ArrayList<>();

        GoalData<FakeTimestampedValue> patient = new GoalData<>(randomName, randomDescription, randomGoalType);

        validateAllGoalFields(randomName, randomDescription, randomGoalType, emptyRecords, emptyTargetMilestones, patient);
    }

    @Test
    public void withDescriptionWillReturnCopyWithNewDescription() {
        GoalData<FakeTimestampedValue> originalGoalData = new RandomGoalData().generate();
        String newDescription = new RandomString().generate();

        GoalData<FakeTimestampedValue> goalDataWithNewDescription = originalGoalData.withDescription(newDescription);

        assertNotSame(originalGoalData, goalDataWithNewDescription);
        validateAllGoalFields(originalGoalData.name(), newDescription, originalGoalData.goalType(), originalGoalData.records(),
                originalGoalData.targetMilestones(),
                goalDataWithNewDescription);
    }

    @ParameterizedTest
    @MethodSource("withGoalTypeWillReturnCopyWithNewGoalType_data")
    public void withGoalTypeWillReturnCopyWithNewGoalType_data(GoalType originalGoalType, GoalType newGoalType) {
        GoalData<FakeTimestampedValue> originalGoalData = new RandomGoalData().generate().withGoalType(originalGoalType);

        GoalData<FakeTimestampedValue> goalDataWithNewGoalType = originalGoalData.withGoalType(newGoalType);

        assertNotSame(originalGoalData, goalDataWithNewGoalType);
        validateAllGoalFields(originalGoalData.name(), originalGoalData.description(), newGoalType, originalGoalData.records(),
                originalGoalData.targetMilestones(),
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
    public void withRecordsWillReturnCopyWithNewRecords() {
        GoalData<FakeTimestampedValue> originalGoalData = new RandomGoalData().generate();
        ArrayList<FakeTimestampedValue> newRecords = timestampedValueListGenerator_.generate();

        GoalData<FakeTimestampedValue> goalDataWithNewRecords = originalGoalData.withRecords(newRecords);

        assertNotSame(originalGoalData, goalDataWithNewRecords);
        validateAllGoalFields(originalGoalData.name(), originalGoalData.description(), originalGoalData.goalType(), newRecords,
                originalGoalData.targetMilestones(),
                goalDataWithNewRecords);
    }

    @Test
    public void withRecordsWillReturnCopyWithNewTargetMilestones() {
        GoalData<FakeTimestampedValue> originalGoalData = new RandomGoalData().generate();
        ArrayList<FakeTimestampedValue> newTargetMilestones = timestampedValueListGenerator_.generate();

        GoalData<FakeTimestampedValue> goalDataWithNewTargetMilestones = originalGoalData.withTargetMilestones(newTargetMilestones);

        assertNotSame(originalGoalData, goalDataWithNewTargetMilestones);
        validateAllGoalFields(originalGoalData.name(), originalGoalData.description(), originalGoalData.goalType(),
                originalGoalData.records(),
                newTargetMilestones,
                goalDataWithNewTargetMilestones);
    }

    @Test
    public void willReturnRecordsByCopy() {
        GoalData<FakeTimestampedValue> patient = new RandomGoalData().generate();
        ArrayList<FakeTimestampedValue> originalRecords = new ArrayList<>(patient.records());
        FakeTimestampedValue randomTimestampedValue = new RandomFakeTimestampedValue().generate();

        ArrayList<FakeTimestampedValue> recordsFromPatient = patient.records();
        recordsFromPatient.add(randomTimestampedValue);

        assertEquals(originalRecords, patient.records());
    }

    @Test
    public void willReturnTargetMilestonesByCopy() {
        GoalData<FakeTimestampedValue> patient = new RandomGoalData().generate();
        ArrayList<FakeTimestampedValue> originalTargetMilestones = new ArrayList<>(patient.targetMilestones());
        FakeTimestampedValue randomTimestampedValue = new RandomFakeTimestampedValue().generate();

        ArrayList<FakeTimestampedValue> targetMilestonesFromPatient = patient.targetMilestones();
        targetMilestonesFromPatient.add(randomTimestampedValue);

        assertEquals(originalTargetMilestones, patient.targetMilestones());
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
        String name1 = stringGen.generate();
        String name2 = stringGen.generate();
        String description1 = stringGen.generate();
        String description2 = stringGen.generate();
        GoalType goalType1 = GoalType.MINIMIZE;
        GoalType goalType2 = GoalType.MAXIMIZE;
        ArrayList<FakeTimestampedValue> records1 = timestampedValueListGenerator_.generate();
        ArrayList<FakeTimestampedValue> records2 = timestampedValueListGenerator_.generate();
        ArrayList<FakeTimestampedValue> targetMilestones1 = timestampedValueListGenerator_.generate();
        ArrayList<FakeTimestampedValue> targetMilestones2 = timestampedValueListGenerator_.generate();

        return Stream.of(
                // equals
                Arguments.of(new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records1, targetMilestones1), true),
                // different names
                Arguments.of(new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<FakeTimestampedValue>(name2, description1, goalType1, records1, targetMilestones1), false),
                // different descriptions
                Arguments.of(new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<FakeTimestampedValue>(name1, description2, goalType1, records1, targetMilestones1), false),
                // different goalTypes
                Arguments.of(new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<FakeTimestampedValue>(name1, description1, goalType2, records1, targetMilestones1), false),
                // different records
                Arguments.of(new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records2, targetMilestones1), false),
                // different targetMilestones
                Arguments.of(new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records1, targetMilestones1),
                        new GoalData<FakeTimestampedValue>(name1, description1, goalType1, records1, targetMilestones2), false)
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
                                                                                    ArrayList<TimestampedType> expectedRecords,
                                                                                    ArrayList<TimestampedType> expectedTargetMilestones,
                                                                                    GoalData<TimestampedType> actualGoalData) {
        assertEquals(expectedName, actualGoalData.name());
        assertEquals(expectedDescription, actualGoalData.description());
        assertEquals(expectedGoalType, actualGoalData.goalType());
        assertEquals(expectedRecords, actualGoalData.records());
        assertEquals(expectedTargetMilestones, actualGoalData.targetMilestones());
    }
}