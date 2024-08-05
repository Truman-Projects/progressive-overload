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
import truman.progressiveoverload.goalManagement.api.RandomTimestampedValue;
import truman.progressiveoverload.goalManagement.api.TimestampedValue;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;

class AcceptanceGoalManagementModule {

    // intermediate interfaces to appease the mockito gods
    private interface I_FakeValueGoalDataPersistenceSource extends I_GoalDataPersistenceSource<Long> {
    }

    private interface I_FakeValueGoalListener extends I_GoalListener<Long> {
    }

    private final RandomGoalData<Long> goalDataGenerator_ = new RandomGoalData<>(new RandomLong());
    private I_GoalRegistryListener[] goalRegistryListenerList_;
    private I_FakeValueGoalListener[] goalListenerList_;
    private I_FakeValueGoalDataPersistenceSource persistenceSource_;
    private UniqueIdSource idSource_;
    private GoalManagementModule<Long> patient_;

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
        idSource_ = new UniqueIdSource(new RandomLongGenerator());
        resetPatient();
    }

    public void resetPatient() {
        patient_ = new GoalManagementModule<>(persistenceSource_, idSource_);

    }

    @Test
    public void initializesModuleUsingPersistenceData() {
        HashMap<Long, GoalData<Long>> persistenceData =
                new RandomHashMap<>(new RandomLong(), goalDataGenerator_).generate();
        when(persistenceSource_.loadGoalDataFromMemory()).thenReturn(persistenceData);
        HashSet<Long> expectedGoalIdSet = new HashSet<>(persistenceData.keySet());

        resetPatient();
        I_GoalRegistryNotifier<Long> goalRegistryNotifier = patient_.goalRegistryNotifier();

        assertEquals(expectedGoalIdSet, goalRegistryNotifier.currentGoalIds());
    }

    @Test
    public void willNotifyGoalRegistryListenersWhenSetOfGoalChanges() {
        GoalData<Long> goalData = goalDataGenerator_.generate();
        patient_.goalRegistryNotifier().registerListener(goalRegistryListenerList_[0]);
        patient_.goalRegistryNotifier().registerListener(goalRegistryListenerList_[1]);

        Long goalId = patient_.goalRegistryUpdater().addGoal(goalData);

        verify(goalRegistryListenerList_[0], times(1)).goalAdded(goalId);
        verify(goalRegistryListenerList_[1], times(1)).goalAdded(goalId);
    }

    @Test
    public void willNotifyGoalListenersWhenGoalChanges() {
        GoalData<Long> goalData = goalDataGenerator_.generate();
        TimestampedValue<Long> milestone = new RandomTimestampedValue<>(new RandomLong()).generate();
        I_GoalRegistryNotifier<Long> goalRegistryNotifier = patient_.goalRegistryNotifier();
        I_GoalRegistryUpdater<Long> goalRegistryUpdater = patient_.goalRegistryUpdater();
        Long goalId = patient_.goalRegistryUpdater().addGoal(goalData);
        try {
            I_GoalNotifier<Long> goalNotifier = goalRegistryNotifier.goalUpdateNotifierByGoalId(goalId);
            I_GoalUpdater<Long> goalUpdater = goalRegistryUpdater.goalUpdaterByGoalId(goalId);
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