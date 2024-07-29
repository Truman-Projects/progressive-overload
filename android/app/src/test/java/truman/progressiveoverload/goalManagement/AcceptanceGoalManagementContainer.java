package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.custom.RandomTimestampedCustomValue;
import truman.progressiveoverload.measurement.custom.TimestampedCustomValue;
import truman.progressiveoverload.measurement.distance.RandomTimestampedDistance;
import truman.progressiveoverload.measurement.distance.TimestampedDistance;
import truman.progressiveoverload.measurement.duration.RandomTimestampedDuration;
import truman.progressiveoverload.measurement.duration.TimestampedDuration;
import truman.progressiveoverload.measurement.mass.RandomTimestampedMass;
import truman.progressiveoverload.measurement.mass.TimestampedMass;
import truman.progressiveoverload.measurement.velocity.RandomTimestampedVelocity;
import truman.progressiveoverload.measurement.velocity.TimestampedVelocity;
import truman.progressiveoverload.randomUtilities.RandomInt;

class AcceptanceGoalManagementContainer {

    // intermediate interfaces for Mockito
    private interface I_FakeMassGoalPersistenceSource extends I_GoalDataPersistenceSource<TimestampedMass> {
    }

    private interface I_FakeDistanceGoalPersistenceSource extends I_GoalDataPersistenceSource<TimestampedDistance> {
    }

    private interface I_FakeDurationGoalPersistenceSource extends I_GoalDataPersistenceSource<TimestampedDuration> {
    }

    private interface I_FakeVelocityGoalPersistenceSource extends I_GoalDataPersistenceSource<TimestampedVelocity> {
    }

    private interface I_FakeCustomGoalPersistenceSource extends I_GoalDataPersistenceSource<TimestampedCustomValue> {
    }

    private I_FakeMassGoalPersistenceSource mockMassPersistenceSource_;
    private I_FakeDistanceGoalPersistenceSource mockDistancePersistenceSource_;
    private I_FakeDurationGoalPersistenceSource mockDurationPersistenceSource_;
    private I_FakeVelocityGoalPersistenceSource mockVelocityPersistenceSource_;
    private I_FakeCustomGoalPersistenceSource mockCustomPersistenceSource_;
    private GoalManagementContainer patient_;

    @BeforeEach
    public void resetEverything() {
        mockMassPersistenceSource_ = mock(I_FakeMassGoalPersistenceSource.class);
        mockDistancePersistenceSource_ = mock(I_FakeDistanceGoalPersistenceSource.class);
        mockDurationPersistenceSource_ = mock(I_FakeDurationGoalPersistenceSource.class);
        mockVelocityPersistenceSource_ = mock(I_FakeVelocityGoalPersistenceSource.class);
        mockCustomPersistenceSource_ = mock(I_FakeCustomGoalPersistenceSource.class);

        patient_ = new GoalManagementContainer(mockMassPersistenceSource_, mockDistancePersistenceSource_, mockDurationPersistenceSource_,
                mockVelocityPersistenceSource_, mockCustomPersistenceSource_);
    }

    @Test
    @Disabled("Disabled until feature implemented")
    public void willNotRepeatGoalIdsAcrossModules() {
        int numberOfGoalsToGeneratePerModule = new RandomInt().generate(5, 10);
        ArrayList<Long> allGoalIds = new ArrayList<>();
        for (int i = 0; i < numberOfGoalsToGeneratePerModule; i++) {
            RandomGoalData<TimestampedMass> massGoalGenerator = new RandomGoalData<>(new RandomTimestampedMass());
            Long id = patient_.massModule().goalRegistryUpdater().addGoal(massGoalGenerator.generate());
            allGoalIds.add(id);
        }
        for (int i = 0; i < numberOfGoalsToGeneratePerModule; i++) {
            RandomGoalData<TimestampedDistance> distanceGoalGenerator = new RandomGoalData<>(new RandomTimestampedDistance());
            Long id = patient_.distanceModule().goalRegistryUpdater().addGoal(distanceGoalGenerator.generate());
            allGoalIds.add(id);
        }
        for (int i = 0; i < numberOfGoalsToGeneratePerModule; i++) {
            RandomGoalData<TimestampedDuration> durationGoalGenerator = new RandomGoalData<>(new RandomTimestampedDuration());
            Long id = patient_.durationModule().goalRegistryUpdater().addGoal(durationGoalGenerator.generate());
            allGoalIds.add(id);
        }
        for (int i = 0; i < numberOfGoalsToGeneratePerModule; i++) {
            RandomGoalData<TimestampedVelocity> velocityGoalGenerator = new RandomGoalData<>(new RandomTimestampedVelocity());
            Long id = patient_.velocityModule().goalRegistryUpdater().addGoal(velocityGoalGenerator.generate());
            allGoalIds.add(id);
        }
        for (int i = 0; i < numberOfGoalsToGeneratePerModule; i++) {
            RandomGoalData<TimestampedCustomValue> customUnitGoalGenerator = new RandomGoalData<>(new RandomTimestampedCustomValue());
            Long id = patient_.customUnitModule().goalRegistryUpdater().addGoal(customUnitGoalGenerator.generate());
            allGoalIds.add(id);
        }
        verifyIdsAreUnique(allGoalIds);

    }

    private void verifyIdsAreUnique(ArrayList<Long> idList) {
        Set<Long> idSet = new HashSet<>();
        for (Long id : idList) {
            boolean result = idSet.add(id);
            if (!result) {
                fail("Duplicate ID found: " + id);
            }
        }
    }

}