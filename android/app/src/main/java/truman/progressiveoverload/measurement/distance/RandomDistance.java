package truman.progressiveoverload.measurement.distance;

import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class RandomDistance implements I_RandomValueGenerator<Distance> {
    public Distance generate() {
        long randomCentimeters = new RandomLong().generate(Distance.MIN_VALUE_CENTIMETERS, Distance.MAX_VALUE_CENTIMETERS);
        return new Distance(randomCentimeters);
    }
}
