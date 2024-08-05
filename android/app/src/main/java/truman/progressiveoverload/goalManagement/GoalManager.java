package truman.progressiveoverload.goalManagement;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import truman.progressiveoverload.goalManagement.api.GoalData;
import truman.progressiveoverload.goalManagement.api.GoalType;
import truman.progressiveoverload.goalManagement.api.I_GoalListener;
import truman.progressiveoverload.goalManagement.api.InvalidQueryException;
import truman.progressiveoverload.goalManagement.api.TimestampedValue;

class GoalManager<GoalFlavour> implements I_GoalManager<GoalFlavour> {
    private GoalData<GoalFlavour> goalData_;
    private Long largestRecordId_;
    private Long largestMilestoneId_;
    private final HashSet<I_GoalListener<GoalFlavour>> listeners_;

    public GoalManager(GoalData<GoalFlavour> data) {
        goalData_ = data;

        Set<Long> currentRecordIds = goalData_.recordsById().keySet();
        largestRecordId_ = currentRecordIds.isEmpty() ? -1L : Collections.max(currentRecordIds);

        Set<Long> currentMilestoneIds = goalData_.targetMilestonesById().keySet();
        largestMilestoneId_ = currentMilestoneIds.isEmpty() ? -1L : Collections.max(currentMilestoneIds);

        listeners_ = new HashSet<>();
    }

    // I_GoalUpdateNotifier
    @Override
    public void registerListener(I_GoalListener<GoalFlavour> listener) {
        listeners_.add(listener);
    }

    @Override
    public void unregisterListener(I_GoalListener<GoalFlavour> listener) {
        listeners_.remove(listener);
    }

    @Override
    public GoalData<GoalFlavour> currentState() {
        return goalData_;
    }

    // I_GoalUpdater
    @Override
    public void changeGoalName(String newName) {
        final String originalName = goalData_.name();
        goalData_ = goalData_.withName(newName);
        if (!originalName.equals(newName)) {
            notifyAllListeners(listener -> listener.goalNameChanged(newName));
        }
    }

    @Override
    public void changeGoalDescription(String newDescription) {
        final String originalDescription = goalData_.description();
        goalData_ = goalData_.withDescription(newDescription);
        if (!originalDescription.equals(newDescription)) {
            notifyAllListeners(listener -> listener.goalDescriptionChanged(newDescription));
        }
    }

    @Override
    public void changeGoalType(GoalType newGoalType) {
        final GoalType originalGoalType = goalData_.goalType();
        goalData_ = goalData_.withGoalType(newGoalType);
        if (!originalGoalType.equals(newGoalType)) {
            notifyAllListeners((listener -> listener.goalTypeChanged(newGoalType)));
        }
    }

    @Override
    public Long addRecord(TimestampedValue<GoalFlavour> record) {
        HashMap<Long, TimestampedValue<GoalFlavour>> records = goalData_.recordsById();
        Long newRecordId = largestRecordId_ + 1;
        records.put(newRecordId, record);
        goalData_ = goalData_.withRecordsById(records);
        largestRecordId_ = newRecordId;

        notifyAllListeners(listener -> listener.recordAdded(newRecordId, record));

        return newRecordId;
    }

    @Override
    public void removeRecord(Long recordId) throws InvalidQueryException {
        HashMap<Long, TimestampedValue<GoalFlavour>> records = goalData_.recordsById();
        if (records.containsKey(recordId)) {
            records.remove(recordId);
            goalData_ = goalData_.withRecordsById(records);

            notifyAllListeners(listener -> listener.recordRemoved(recordId));
        } else {
            throw new InvalidQueryException("Attempting to remove nonexistent record ID");
        }
    }

    @Override
    public void editRecord(Long recordId, TimestampedValue<GoalFlavour> updatedRecord) throws InvalidQueryException {
        HashMap<Long, TimestampedValue<GoalFlavour>> records = goalData_.recordsById();
        if (records.containsKey(recordId)) {
            records.remove(recordId);
            records.put(recordId, updatedRecord);
            goalData_ = goalData_.withRecordsById(records);

            notifyAllListeners(listener -> listener.recordChanged(recordId, updatedRecord));
        } else {
            throw new InvalidQueryException("Attempting to edit record with nonexistent record ID");
        }
    }

    @Override
    public Long addTargetMilestone(TimestampedValue<GoalFlavour> targetMilestone) {
        HashMap<Long, TimestampedValue<GoalFlavour>> milestones = goalData_.targetMilestonesById();
        Long newMilestoneId = largestMilestoneId_ + 1;
        milestones.put(newMilestoneId, targetMilestone);
        goalData_ = goalData_.withTargetMilestonesById(milestones);
        largestMilestoneId_ = newMilestoneId;

        notifyAllListeners(listener -> listener.targetMilestoneAdded(newMilestoneId, targetMilestone));
        return newMilestoneId;
    }

    @Override
    public void removeTargetMilestone(Long milestoneId) throws InvalidQueryException {
        HashMap<Long, TimestampedValue<GoalFlavour>> milestones = goalData_.targetMilestonesById();
        if (milestones.containsKey(milestoneId)) {
            milestones.remove(milestoneId);
            goalData_ = goalData_.withTargetMilestonesById(milestones);

            notifyAllListeners(listener -> listener.targetMilestoneRemoved(milestoneId));
        } else {
            throw new InvalidQueryException("Attempting to remove nonexistent milestone ID");
        }

    }

    @Override
    public void editTargetMilestone(Long milestoneId, TimestampedValue<GoalFlavour> updatedMilestone) throws InvalidQueryException {
        HashMap<Long, TimestampedValue<GoalFlavour>> milestones = goalData_.targetMilestonesById();
        if (milestones.containsKey(milestoneId)) {
            milestones.remove(milestoneId);
            milestones.put(milestoneId, updatedMilestone);
            goalData_ = goalData_.withTargetMilestonesById(milestones);

            notifyAllListeners(listener -> listener.targetMilestoneChanged(milestoneId, updatedMilestone));
        } else {
            throw new InvalidQueryException("Attempting to edit target milestone with nonexistent milestone ID");
        }
    }

    private interface NotifyListenersLambda<T> {
        void notify(I_GoalListener<T> listener);
    }

    private void notifyAllListeners(NotifyListenersLambda<GoalFlavour> notifyFunction) {
        for (I_GoalListener<GoalFlavour> listener : listeners_) {
            notifyFunction.notify(listener);
        }
    }
}
