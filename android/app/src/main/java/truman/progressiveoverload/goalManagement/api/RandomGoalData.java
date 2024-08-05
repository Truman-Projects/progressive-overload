package truman.progressiveoverload.goalManagement.api;

import java.util.HashMap;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomEnum;
import truman.progressiveoverload.randomUtilities.RandomHashMap;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomString;

public class RandomGoalData<GoalFlavour> implements I_RandomValueGenerator<GoalData<GoalFlavour>> {
    private final RandomHashMap<Long, TimestampedValue<GoalFlavour>> timestampedValueHashMapGenerator_;

    public RandomGoalData(I_RandomValueGenerator<GoalFlavour> goalFlavourGenerator) {
        RandomTimestampedValue<GoalFlavour> timestampedValueGenerator = new RandomTimestampedValue<>(goalFlavourGenerator);
        timestampedValueHashMapGenerator_ = new RandomHashMap<>(new RandomLong(), timestampedValueGenerator);
    }

    public GoalData<GoalFlavour> generate() {
        String randomName = new RandomString().generate();
        String randomDescription = new RandomString().generate();
        GoalType randomGoalType = new RandomEnum<>(GoalType.class).generate();
        HashMap<Long, TimestampedValue<GoalFlavour>> randomRecords = timestampedValueHashMapGenerator_.generate();
        HashMap<Long, TimestampedValue<GoalFlavour>> randomTargetMilestones =
                timestampedValueHashMapGenerator_.generate();
        return new GoalData<>(randomName, randomDescription, randomGoalType, randomRecords, randomTargetMilestones);
    }
}
