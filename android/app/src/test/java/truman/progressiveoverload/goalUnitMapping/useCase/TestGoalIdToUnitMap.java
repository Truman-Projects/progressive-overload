package truman.progressiveoverload.goalUnitMapping.useCase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalUnitMapping.useCase.api.GoalUnit;
import truman.progressiveoverload.measurement.custom.TimestampedCustomValue;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.TimestampedMass;
import truman.progressiveoverload.measurement.velocity.TimestampedVelocity;
import truman.progressiveoverload.randomUtilities.RandomHashSet;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestGoalIdToUnitMap {

    private interface I_MassGoalRegistryNotifier extends I_GoalRegistryNotifier<TimestampedMass> {
    }

    private interface I_DistanceGoalRegistryNotifier extends I_GoalRegistryNotifier<TimestampedDistance> {
    }

    private interface I_DurationGoalRegistryNotifier extends I_GoalRegistryNotifier<TimestampedDuration> {
    }

    private interface I_VelocityGoalRegistryNotifier extends I_GoalRegistryNotifier<TimestampedVelocity> {
    }

    private interface I_CustomGoalRegistryNotifier extends I_GoalRegistryNotifier<TimestampedCustomValue> {
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

    private GoalIdToUnitMap patient_;

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

        patient_ = new GoalIdToUnitMap(mockMassGoalRegistryNotifier_, mockDistanceGoalRegistryNotifier_,
                mockDurationGoalRegistryNotifier_, mockVelocityGoalRegistryNotifier_, mockCustomGoalRegistryNotifier_);

    }

    @Test
    public void willReturnInvalidUnitForNonExistentGoalIds() {
        Long randomGoalId = new RandomLong().generate();

        GoalUnit actualGoalUnit = patient_.unitForGoalId(randomGoalId);

        assertEquals(GoalUnit.INVALID, actualGoalUnit);
    }

    // poor man's data driven test (proper parameterized test not possible because method source must be static)
    private void testRemovingGoalId(I_GoalRegistryListener listener) {
        Long goalId = new RandomLong().generate();
        listener.goalAdded(goalId);

        listener.goalRemoved(goalId);

        assertEquals(GoalUnit.INVALID, patient_.unitForGoalId(goalId));
    }

    @Test
    public void willReturnInvalidUnitForRemovedMassGoalId() {
        testRemovingGoalId(massGoalListener_);
    }

    @Test
    public void willReturnInvalidUnitForRemovedDistanceGoalId() {
        testRemovingGoalId(distanceGoalListener_);
    }

    @Test
    public void willReturnInvalidUnitForRemovedDurationGoalId() {
        testRemovingGoalId(durationGoalListener_);
    }

    @Test
    public void willReturnInvalidUnitForRemovedVelocityGoalId() {
        testRemovingGoalId(velocityGoalListener_);
    }

    @Test
    public void willReturnInvalidUnitForRemovedCustomGoalId() {
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
    public void willMapGoalIdsToCorrectUnits() {
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

        assertEquals(GoalUnit.MASS, patient_.unitForGoalId(massGoalId));
        assertEquals(GoalUnit.DISTANCE, patient_.unitForGoalId(distanceGoalId));
        assertEquals(GoalUnit.DURATION, patient_.unitForGoalId(durationGoalId));
        assertEquals(GoalUnit.VELOCITY, patient_.unitForGoalId(velocityGoalId));
        assertEquals(GoalUnit.CUSTOM, patient_.unitForGoalId(customGoalId));
    }
}