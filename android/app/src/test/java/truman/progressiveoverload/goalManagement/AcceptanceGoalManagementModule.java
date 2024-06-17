package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.I_GoalDataPersistenceSource;
import truman.progressiveoverload.goalManagement.api.I_GoalListener;
import truman.progressiveoverload.goalManagement.api.I_GoalNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryListener;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryNotifier;
import truman.progressiveoverload.goalManagement.api.I_GoalRegistryUpdater;
import truman.progressiveoverload.goalManagement.api.I_GoalUpdater;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.goalManagement.api.RandomGoalData;
import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.measurement.RandomFakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;

class AcceptanceGoalManagementModule {

    // intermediate interfaces to appease the mockito gods
    private interface I_FakeValueGoalDataPersistenceSource extends I_GoalDataPersistenceSource<FakeTimestampedValue> {
    }

    private interface I_FakeValueGoalListener extends I_GoalListener<FakeTimestampedValue> {
    }

    private I_GoalRegistryListener[] goalRegistryListenerList_;
    private I_FakeValueGoalListener[] goalListenerList_;
    private I_FakeValueGoalDataPersistenceSource persistenceSource_;
    private GoalManagementModule<FakeTimestampedValue> patient_;

    @BeforeEach
    public void resetEverything() {
        goalRegistryListenerList_ = new I_GoalRegistryListener[]{
                mock(I_GoalRegistryListener.class),
                mock(I_GoalRegistryListener.class)
        };
        goalListenerList_ = new I_FakeValueGoalListener[]{
                mock(I_FakeValueGoalListener.class),
                mock(I_FakeValueGoalListener.class)
        };
        persistenceSource_ = mock(I_FakeValueGoalDataPersistenceSource.class);
        resetPatient();
    }

    public void resetPatient() {
        patient_ = new GoalManagementModule<>(persistenceSource_);

    }

    @Test
    public void initializesModuleUsingPersistenceData() {
        HashMap<Long, GoalData<FakeTimestampedValue>> persistenceData =
                new RandomHashMap<>(new RandomLong(), new RandomGoalData()).generate();
        when(persistenceSource_.loadGoalDataFromMemory()).thenReturn(persistenceData);
        HashSet<Long> expectedGoalIdSet = new HashSet<>(persistenceData.keySet());

        resetPatient();
        I_GoalRegistryNotifier<FakeTimestampedValue> goalRegistryNotifier = patient_.goalRegistryNotifier();

        assertEquals(expectedGoalIdSet, goalRegistryNotifier.currentGoalIds());
    }

    @Test
    public void willNotifyGoalRegistryListenersWhenSetOfGoalChanges() {
        GoalData<FakeTimestampedValue> goalData = new RandomGoalData().generate();
        patient_.goalRegistryNotifier().registerListener(goalRegistryListenerList_[0]);
        patient_.goalRegistryNotifier().registerListener(goalRegistryListenerList_[1]);

        Long goalId = patient_.goalRegistryUpdater().addGoal(goalData);

        verify(goalRegistryListenerList_[0], times(1)).goalAdded(goalId);
        verify(goalRegistryListenerList_[1], times(1)).goalAdded(goalId);
    }

    @Test
    public void willNotifyGoalListenersWhenGoalChanges() {
        GoalData<FakeTimestampedValue> goalData = new RandomGoalData().generate();
        FakeTimestampedValue milestone = new RandomFakeTimestampedValue().generate();
        I_GoalRegistryNotifier<FakeTimestampedValue> goalRegistryNotifier = patient_.goalRegistryNotifier();
        I_GoalRegistryUpdater<FakeTimestampedValue> goalRegistryUpdater = patient_.goalRegistryUpdater();
        Long goalId = patient_.goalRegistryUpdater().addGoal(goalData);
        try {
            I_GoalNotifier<FakeTimestampedValue> goalNotifier = goalRegistryNotifier.goalUpdateNotifierByGoalId(goalId);
            I_GoalUpdater<FakeTimestampedValue> goalUpdater = goalRegistryUpdater.goalUpdaterByGoalId(goalId);
            goalNotifier.registerListener(goalListenerList_[0]);
            goalNotifier.registerListener(goalListenerList_[1]);

            Long milestoneId = goalUpdater.addTargetMilestone(milestone);

            verify(goalListenerList_[0], times(1)).targetMilestoneAdded(milestoneId, milestone);
            verify(goalListenerList_[1], times(1)).targetMilestoneAdded(milestoneId, milestone);

        } catch (InvalidQueryException e) {
            fail("Caught unexpected exception");
        }
    }
}