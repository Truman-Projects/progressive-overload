package truman.progressiveoverload.goalManagement.api;

import java.util.HashMap;

import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.measurement.RandomFakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomEnum;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomString;

public class RandomGoalData implements I_RandomValueGenerator<GoalData<FakeTimestampedValue>> {
    private static final RandomHashMap<Long, FakeTimestampedValue> timestampedValueHashMapGenerator_ =
            new RandomHashMap<>(new RandomLong(), new RandomFakeTimestampedValue());

    public GoalData<FakeTimestampedValue> generate() {
        String randomName = new RandomString().generate();
        String randomDescription = new RandomString().generate();
        GoalType randomGoalType = new RandomEnum<>(GoalType.class).generate();
        HashMap<Long, FakeTimestampedValue> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, FakeTimestampedValue> randomTargetMilestones =
                timestampedValueHashMapGenerator_.generate();
        return new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords, randomTargetMilestones);
    }
}
