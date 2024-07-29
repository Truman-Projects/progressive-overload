package truman.progressiveoverload.goalManagement.api;

import java.util.HashMap;

import truman.progressiveoverload.measurement.I_TimestampedValue;
import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomEnum;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomString;

public class RandomGoalData<TimestampedType extends I_TimestampedValue> implements I_RandomValueGenerator<GoalData<TimestampedType>> {
    private final RandomHashMap<Long, TimestampedType> timestampedValueHashMapGenerator_;

    public RandomGoalData(I_RandomValueGenerator<TimestampedType> timestampedTypeGenerator) {
        timestampedValueHashMapGenerator_ = new RandomHashMap<>(new RandomLong(), timestampedTypeGenerator);
    }

    public GoalData<TimestampedType> generate() {
        String randomName = new RandomString().generate();
        String randomDescription = new RandomString().generate();
        GoalType randomGoalType = new RandomEnum<>(GoalType.class).generate();
        HashMap<Long, TimestampedType> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedType> randomTargetMilestones =
                timestampedValueHashMapGenerator_.generate();
        return new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords, randomTargetMilestones);
    }
}
