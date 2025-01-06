package truman.progressiveoverload.measurement.distance;

import javarandoms.I_RandomValueGenerator;
import javarandoms.RandomLong;

public class RandomDistance implements I_RandomValueGenerator<Distance> {
    public Distance generate() {
        long randomCentimeters = new RandomLong().generate(Distance.MIN_VALUE_CENTIMETERS, Distance.MAX_VALUE_CENTIMETERS);
        return Distance.fromCentimeters(randomCentimeters);
    }
}
