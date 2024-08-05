package truman.progressiveoverload.goalWriting.useCase;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.GoalType;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;
import truman.progressiveoverload.goalFlavours.useCase.api.GoalFlavour;
import truman.progressiveoverload.goalWriting.useCase.api.GoalPolarity;
import truman.progressiveoverload.measurement.I_TimestampedValue;
import truman.progressiveoverload.measurement.custom.TimestampedCustomValue;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;
import truman.progressiveoverload.measurement.velocity.TimestampedVelocity;
import truman.progressiveoverload.randomUtilities.RandomEnum;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomString;

public class TestGoalWriter {
    private interface I_MockMassGoalRegistryUpdater extends I_GoalRegistryUpdater<TimestampedMass> {
    }

    private interface I_MockDistanceGoalRegistryUpdater extends I_GoalRegistryUpdater<TimestampedDistance> {
    }

    private interface I_MockDurationGoalRegistryUpdater extends I_GoalRegistryUpdater<TimestampedDuration> {
    }

    private interface I_MockVelocityGoalRegistryUpdater extends I_GoalRegistryUpdater<TimestampedVelocity> {
    }

    private interface I_MockCustomGoalRegistryUpdater extends I_GoalRegistryUpdater<TimestampedCustomValue> {
    }

    private I_MockMassGoalRegistryUpdater mockMassGoalRegistryUpdater_;
    private I_MockDistanceGoalRegistryUpdater mockDistanceGoalRegistryUpdater_;
    private I_MockDurationGoalRegistryUpdater mockDurationGoalRegistryUpdater_;
    private I_MockVelocityGoalRegistryUpdater mockVelocityGoalRegistryUpdater_;
    private I_MockCustomGoalRegistryUpdater mockCustomGoalRegistryUpdater_;
    private GoalWriter patient_;

    @BeforeEach
    public void resetEverything() {
        mockMassGoalRegistryUpdater_ = mock(I_MockMassGoalRegistryUpdater.class);
        mockDistanceGoalRegistryUpdater_ = mock(I_MockDistanceGoalRegistryUpdater.class);
        mockDurationGoalRegistryUpdater_ = mock(I_MockDurationGoalRegistryUpdater.class);
        mockVelocityGoalRegistryUpdater_ = mock(I_MockVelocityGoalRegistryUpdater.class);
        mockCustomGoalRegistryUpdater_ = mock(I_MockCustomGoalRegistryUpdater.class);
        recreatePatient();
    }

    private void recreatePatient() {
        patient_ = new GoalWriter(mockMassGoalRegistryUpdater_, mockDistanceGoalRegistryUpdater_, mockDurationGoalRegistryUpdater_,
                mockVelocityGoalRegistryUpdater_, mockCustomGoalRegistryUpdater_);
    }

    private <T extends I_TimestampedValue> void testGoalCreation(GoalPolarity goalPolarity, GoalType expectedGoalType,
                                                                 I_GoalRegistryUpdater<T> goalRegistryUpdater, GoalFlavour goalFlavour) {
        RandomString stringGen = new RandomString();
        String randomName = stringGen.generate();
        String randomDescription = stringGen.generate();
        GoalData<T> expectedGoalData = new GoalData<>(randomName, randomDescription, expectedGoalType);

        patient_.createGoal(randomName, randomDescription, goalPolarity, goalFlavour);

        verify(goalRegistryUpdater, times(1)).addGoal(expectedGoalData);
    }

    private void testGoalIdOutput(I_GoalRegistryUpdater<?> goalRegistryUpdater, GoalFlavour goalFlavour) {
        RandomString stringGen = new RandomString();
        String randomName = stringGen.generate();
        String randomDescription = stringGen.generate();
        GoalPolarity randomGoalPolarity = new RandomEnum<>(GoalPolarity.class).generate();
        Long expectedGoalId = new RandomLong().generate();
        when(goalRegistryUpdater.addGoal(any(GoalData.class))).thenReturn(expectedGoalId);

        Long actualGoalId = patient_.createGoal(randomName, randomDescription, randomGoalPolarity, goalFlavour);

        assertEquals(expectedGoalId, actualGoalId);
    }


    @ParameterizedTest
    @MethodSource("goalPolarityToGoalTypeMapping")
    public void willCreateMassGoal(GoalPolarity goalPolarity, GoalType expectedGoalType) {
        testGoalCreation(goalPolarity, expectedGoalType, mockMassGoalRegistryUpdater_, GoalFlavour.MASS);
    }

    @Test
    public void willReturnCorrectGoalIdWhenCreatingMassGoal() {
        testGoalIdOutput(mockMassGoalRegistryUpdater_, GoalFlavour.MASS);
    }

    @ParameterizedTest
    @MethodSource("goalPolarityToGoalTypeMapping")
    public void willCreateDistanceGoal(GoalPolarity goalPolarity, GoalType expectedGoalType) {
        testGoalCreation(goalPolarity, expectedGoalType, mockDistanceGoalRegistryUpdater_, GoalFlavour.DISTANCE);
    }

    @Test
    public void willReturnCorrectGoalIdWhenCreatingDistanceGoal() {
        testGoalIdOutput(mockDistanceGoalRegistryUpdater_, GoalFlavour.DISTANCE);
    }

    @ParameterizedTest
    @MethodSource("goalPolarityToGoalTypeMapping")
    public void willCreateDurationGoal(GoalPolarity goalPolarity, GoalType expectedGoalType) {
        testGoalCreation(goalPolarity, expectedGoalType, mockDurationGoalRegistryUpdater_, GoalFlavour.DURATION);
    }

    @Test
    public void willReturnCorrectGoalIdWhenCreatingDurationGoal() {
        testGoalIdOutput(mockDurationGoalRegistryUpdater_, GoalFlavour.DURATION);
    }

    @ParameterizedTest
    @MethodSource("goalPolarityToGoalTypeMapping")
    public void willCreateVelocityGoal(GoalPolarity goalPolarity, GoalType expectedGoalType) {
        testGoalCreation(goalPolarity, expectedGoalType, mockVelocityGoalRegistryUpdater_, GoalFlavour.VELOCITY);
    }

    @Test
    public void willReturnCorrectGoalIdWhenCreatingVelocityGoal() {
        testGoalIdOutput(mockVelocityGoalRegistryUpdater_, GoalFlavour.VELOCITY);
    }

    @ParameterizedTest
    @MethodSource("goalPolarityToGoalTypeMapping")
    public void willCreateCustomGoal(GoalPolarity goalPolarity, GoalType expectedGoalType) {
        testGoalCreation(goalPolarity, expectedGoalType, mockCustomGoalRegistryUpdater_, GoalFlavour.CUSTOM);
    }

    @Test
    public void willReturnCorrectGoalIdWhenCreatingCustomGoal() {
        testGoalIdOutput(mockCustomGoalRegistryUpdater_, GoalFlavour.CUSTOM);
    }

    // returns (GoalPolarity goalPolarity, GoalType expectedGoalType)
    private static Stream<Arguments> goalPolarityToGoalTypeMapping() {
        return Stream.of(
                Arguments.of(GoalPolarity.MINIMIZE, GoalType.MINIMIZE),
                Arguments.of(GoalPolarity.MAXIMIZE, GoalType.MAXIMIZE)
        );
    }
}
