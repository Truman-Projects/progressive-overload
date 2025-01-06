package truman.progressiveoverload.measurement.mass;

import javarandoms.I_RandomValueGenerator;
import javarandoms.RandomLong;

public class RandomMass implements I_RandomValueGenerator<Mass> {
    public Mass generate() {
        long randomMilligrams = new RandomLong().generate(Mass.MIN_VALUE_MILLIGRAMS, Mass.MAX_VALUE_MILLIGRAMS);
        return Mass.fromMilligrams(randomMilligrams);
    }
}