package truman.progressiveoverload.goalFlavours.useCase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalFlavours.useCase.api.GoalFlavour;
import truman.progressiveoverload.measurement.distance.Distance;
import truman.progressiveoverload.measurement.mass.Mass;
import truman.progressiveoverload.measurement.velocity.Velocity;
import truman.progressiveoverload.randomUtilities.RandomHashSet;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestGoalIdToFlavourMap {

    private interface I_MassGoalRegistryNotifier extends I_GoalRegistryNotifier<Mass> {
    }

    private interface I_DistanceGoalRegistryNotifier extends I_GoalRegistryNotifier<Distance> {
    }

    private interface I_DurationGoalRegistryNotifier extends I_GoalRegistryNotifier<Duration> {
    }

    private interface I_VelocityGoalRegistryNotifier extends I_GoalRegistryNotifier<Velocity> {
    }

    private interface I_CustomGoalRegistryNotifier extends I_GoalRegistryNotifier<Double> {
    }

    private I_MassGoalRegistryNotifier mockMassGoalRegistryNotifier_;
    private I_DistanceGoalRegistryNotifier mockDistanceGoalRegistryNotifier_;
    private I_DurationGoalRegistryNotifier mockDurationGoalRegistryNotifier_;
    private I_VelocityGoalRegistryNotifier mockVelocityGoalRegistryNotifier_;
    private I_CustomGoalRegistryNotifier mockCustomGoalRegistryNotifier_;

    private I_GoalRegistryListener massGoalListener_;
    private I_GoalRegistryListener distanceGoalListener_;
    private I_GoalRegistryListener durationGoalListener_;
    private I_GoalRegistryListener velocityGoalListener_;
    private I_GoalRegistryListener customGoalListener_;

    private GoalIdToFlavourMap patient_;

    @BeforeEach
    public void resetEverything() {
        mockMassGoalRegistryNotifier_ = mock(I_MassGoalRegistryNotifier.class);
        mockDistanceGoalRegistryNotifier_ = mock(I_DistanceGoalRegistryNotifier.class);
        mockDurationGoalRegistryNotifier_ = mock(I_DurationGoalRegistryNotifier.class);
        mockVelocityGoalRegistryNotifier_ = mock(I_VelocityGoalRegistryNotifier.class);
        mockCustomGoalRegistryNotifier_ = mock(I_CustomGoalRegistryNotifier.class);

        doAnswer(invocation -> {
            massGoalListener_ = invocation.getArgument(0);
            return null;
        }).when(mockMassGoalRegistryNotifier_).registerListener(any(I_GoalRegistryListener.class));
        doAnswer(invocation -> {
            distanceGoalListener_ = invocation.getArgument(0);
            return null;
        }).when(mockDistanceGoalRegistryNotifier_).registerListener(any(I_GoalRegistryListener.class));
        doAnswer(invocation -> {
            durationGoalListener_ = invocation.getArgument(0);
            return null;
        }).when(mockDurationGoalRegistryNotifier_).registerListener(any(I_GoalRegistryListener.class));
        doAnswer(invocation -> {
            velocityGoalListener_ = invocation.getArgument(0);
            return null;
        }).when(mockVelocityGoalRegistryNotifier_).registerListener(any(I_GoalRegistryListener.class));
        doAnswer(invocation -> {
            customGoalListener_ = invocation.getArgument(0);
            return null;
        }).when(mockCustomGoalRegistryNotifier_).registerListener(any(I_GoalRegistryListener.class));

        recreatePatient();
    }

    private void recreatePatient() {
        clearInvocations(mockMassGoalRegistryNotifier_, mockDistanceGoalRegistryNotifier_, mockDurationGoalRegistryNotifier_,
                mockVelocityGoalRegistryNotifier_, mockCustomGoalRegistryNotifier_);

        patient_ = new GoalIdToFlavourMap(mockMassGoalRegistryNotifier_, mockDistanceGoalRegistryNotifier_,
                mockDurationGoalRegistryNotifier_, mockVelocityGoalRegistryNotifier_, mockCustomGoalRegistryNotifier_);

    }

    @Test
    public void willReturnEmptyFlavourForNonExistentGoalIds() {
        Long randomGoalId = new RandomLong().generate();

        Optional<GoalFlavour> actualGoalFlavour = patient_.flavourForGoalId(randomGoalId);

        assertFalse(actualGoalFlavour.isPresent());
    }

    // poor man's data driven test (proper parameterized test not possible because method source must be static)
    private void testRemovingGoalId(I_GoalRegistryListener listener) {
        Long goalId = new RandomLong().generate();
        listener.goalAdded(goalId);

        listener.goalRemoved(goalId);

        Optional<GoalFlavour> actualGoalFlavour = patient_.flavourForGoalId(goalId);
        assertFalse(actualGoalFlavour.isPresent());
    }

    @Test
    public void willReturnEmptyFlavourForRemovedMassGoalId() {
        testRemovingGoalId(massGoalListener_);
    }

    @Test
    public void willReturnEmptyFlavourForRemovedDistanceGoalId() {
        testRemovingGoalId(distanceGoalListener_);
    }

    @Test
    public void willReturnEmptyFlavourForRemovedDurationGoalId() {
        testRemovingGoalId(durationGoalListener_);
    }

    @Test
    public void willReturnEmptyFlavourForRemovedVelocityGoalId() {
        testRemovingGoalId(velocityGoalListener_);
    }

    @Test
    public void willReturnEmptyFlavourForRemovedCustomGoalId() {
        testRemovingGoalId(customGoalListener_);
    }


    @Test
    public void willSubscribeToGoalRegistryNotifiers() {
        recreatePatient();

        verify(mockMassGoalRegistryNotifier_, times(1)).registerListener(any(I_GoalRegistryListener.class));
        verify(mockDistanceGoalRegistryNotifier_, times(1)).registerListener(any(I_GoalRegistryListener.class));
        verify(mockDurationGoalRegistryNotifier_, times(1)).registerListener(any(I_GoalRegistryListener.class));
        verify(mockVelocityGoalRegistryNotifier_, times(1)).registerListener(any(I_GoalRegistryListener.class));
        verify(mockCustomGoalRegistryNotifier_, times(1)).registerListener(any(I_GoalRegistryListener.class));
    }

    @Test
    public void willMapGoalIdsToCorrectFlavours() {
        ArrayList<Long> randomUniqueIds = new ArrayList<>(new RandomHashSet<>(new RandomLong()).generate(5));
        final Long massGoalId = randomUniqueIds.get(0);
        final Long distanceGoalId = randomUniqueIds.get(1);
        final Long durationGoalId = randomUniqueIds.get(2);
        final Long velocityGoalId = randomUniqueIds.get(3);
        final Long customGoalId = randomUniqueIds.get(4);
        massGoalListener_.goalAdded(massGoalId);
        distanceGoalListener_.goalAdded(distanceGoalId);
        durationGoalListener_.goalAdded(durationGoalId);
        velocityGoalListener_.goalAdded(velocityGoalId);
        customGoalListener_.goalAdded(customGoalId);

        assertPresentAndEquals(GoalFlavour.MASS, patient_.flavourForGoalId(massGoalId));
        assertPresentAndEquals(GoalFlavour.DISTANCE, patient_.flavourForGoalId(distanceGoalId));
        assertPresentAndEquals(GoalFlavour.DURATION, patient_.flavourForGoalId(durationGoalId));
        assertPresentAndEquals(GoalFlavour.VELOCITY, patient_.flavourForGoalId(velocityGoalId));
        assertPresentAndEquals(GoalFlavour.CUSTOM, patient_.flavourForGoalId(customGoalId));
    }

    private void testMappingPreviousGoalIds(GoalFlavour expectedGoalFlavour, I_GoalRegistryNotifier<?> mockGoalSource) {
        HashSet<Long> goalIds = new RandomHashSet<>(new RandomLong()).generate();
        when(mockGoalSource.currentGoalIds()).thenReturn(goalIds);

        recreatePatient();

        for (Long goalId : goalIds) {
            assertPresentAndEquals(expectedGoalFlavour, patient_.flavourForGoalId(goalId));
        }
    }

    @Test
    public void willCorrectlyMapPreviousMassGoalIds() {
        testMappingPreviousGoalIds(GoalFlavour.MASS, mockMassGoalRegistryNotifier_);
    }

    @Test
    public void willCorrectlyMapPreviousDistanceGoalIds() {
        testMappingPreviousGoalIds(GoalFlavour.DISTANCE, mockDistanceGoalRegistryNotifier_);
    }

    @Test
    public void willCorrectlyMapPreviousDurationGoalIds() {
        testMappingPreviousGoalIds(GoalFlavour.DURATION, mockDurationGoalRegistryNotifier_);
    }

    @Test
    public void willCorrectlyMapPreviousVelocityGoalIds() {
        testMappingPreviousGoalIds(GoalFlavour.VELOCITY, mockVelocityGoalRegistryNotifier_);
    }

    @Test
    public void willCorrectlyMapPreviousCustomGoalIds() {
        testMappingPreviousGoalIds(GoalFlavour.CUSTOM, mockCustomGoalRegistryNotifier_);
    }

    private <T> void assertPresentAndEquals(T expectedValue, Optional<T> actualOptional) {
        assertTrue(actualOptional.isPresent());
        assertEquals(expectedValue, actualOptional.get());
    }
}