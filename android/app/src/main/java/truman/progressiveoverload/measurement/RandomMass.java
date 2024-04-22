package truman.progressiveoverload.measurement;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomMass implements I_RandomValueGenerator<Mass> {
    public Mass generate() {
        long randomMilligrams = new RandomLong().generate(Mass.MIN_VALUE_MILLIGRAMS, Mass.MAX_VALUE_MILLIGRAMS);
        return new Mass(randomMilligrams);
    }
}