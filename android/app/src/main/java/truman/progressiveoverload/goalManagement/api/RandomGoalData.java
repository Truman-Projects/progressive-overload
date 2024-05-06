package truman.progressiveoverload.goalManagement.api;

import java.util.ArrayList;

import truman.progressiveoverload.measurement.FakeTimestampedValue;
import truman.progressiveoverload.measurement.RandomFakeTimestampedValue;
import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomArrayList;
import truman.progressiveoverload.randomUtilities.RandomEnum;
import truman.progressiveoverload.randomUtilities.RandomString;

public class RandomGoalData implements I_RandomValueGenerator<GoalData<FakeTimestampedValue>> {
    private static final RandomArrayList<FakeTimestampedValue> timestampedValueGenerator_ =
            new RandomArrayList<>(new RandomFakeTimestampedValue());

    public GoalData<FakeTimestampedValue> generate() {
        String randomName = new RandomString().generate();
        String randomDescription = new RandomString().generate();
        GoalType randomGoalType = new RandomEnum<>(GoalType.class).generate();
        ArrayList<FakeTimestampedValue> randomRecords = timestampedValueGenerator_.generate();
        ArrayList<FakeTimestampedValue> randomTargetMilestones =
                timestampedValueGenerator_.generate();
        return new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords, randomTargetMilestones);
    }
}
