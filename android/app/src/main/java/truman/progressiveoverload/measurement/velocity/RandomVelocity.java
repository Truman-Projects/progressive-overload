package truman.progressiveoverload.measurement.velocity;

import truman.progressiveoverload.measurement.distance.Distance;
import truman.progressiveoverload.measurement.distance.RandomDistance;
import truman.progressiveoverload.randomUtilities.I_RandomValueGenerator;

public class RandomVelocity implements I_RandomValueGenerator<Velocity> {
    public Velocity generate() {
        Distance randomDistancePerHour = new RandomDistance().generate();
        return Velocity.fromDistancePerHour(randomDistancePerHour);
    }
}
