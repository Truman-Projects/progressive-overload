package truman.progressiveoverload.measurement.velocity;

import java.time.Duration;

import truman.progressiveoverload.measurement.distance.Distance;

public class Velocity {
    private final Distance distancePerHour_;
    private static final Distance INVALID_VELOCITY_SENTINEL = Distance.fromCentimeters(0);
    private static final Duration INVALID_PACE_SENTINEL = Duration.ofNanos(0);
    private static final Double SECONDS_PER_HOUR = 3600.0;

    private Velocity(Distance distancePerHour) {
        distancePerHour_ = distancePerHour;
    }

    public Distance toDistancePerHour() {
        return distancePerHour_;
    }

    public static Velocity fromDistancePerHour(Distance distancePerHour) {
        return new Velocity(distancePerHour);
    }

    public Duration toTimePerKilometer() {
        try {
            Distance kilometer = Distance.fromKilometers(1);
            return toTimePerDistance(kilometer);
        } catch (Exception e) {
            return INVALID_PACE_SENTINEL;
        }
    }

    public static Velocity fromTimePerKilometer(Duration timePerKilometer) {
        double secondsPerKilometer = convertDurationToDoubleInSeconds(timePerKilometer);
        try {
            double kilometersPerHour = SECONDS_PER_HOUR / secondsPerKilometer;
            Distance distancePerHour = Distance.fromKilometers(kilometersPerHour);
            return new Velocity(distancePerHour);
        } catch (Exception e) {
            return new Velocity(INVALID_VELOCITY_SENTINEL);
        }
    }

    public Duration toTimePerMile() {
        try {
            Distance mile = Distance.fromMiles(1);
            return toTimePerDistance(mile);
        } catch (Exception e) {
            return INVALID_PACE_SENTINEL;
        }
    }

    public static Velocity fromTimePerMile(Duration timePerMile) {
        double secondsPerMile = convertDurationToDoubleInSeconds(timePerMile);
        try {
            double milesPerHour = SECONDS_PER_HOUR / secondsPerMile;
            Distance distancePerHour = Distance.fromMiles(milesPerHour);
            return new Velocity(distancePerHour);
        } catch (Exception e) {
            return new Velocity(INVALID_VELOCITY_SENTINEL);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Velocity)) {
            return false;
        }
        return (this.distancePerHour_.equals(((Velocity) other).toDistancePerHour()));
    }

    @Override
    public String toString() {
        return distancePerHour_.toString() + " per hour";
    }

    private Duration toTimePerDistance(Distance targetDistance) throws ArithmeticException {
        if (distancePerHour_.equals(INVALID_VELOCITY_SENTINEL)) {
            throw new ArithmeticException();
        } else {
            try {
                double targetDistancePerHour = distancePerHour_.divideByDistance(targetDistance);
                double nanosecondsPerHour = Duration.ofHours(1).toNanos();
                double nanosecondsPerTargetDistance = nanosecondsPerHour / targetDistancePerHour;
                return Duration.ofNanos((long) nanosecondsPerTargetDistance);
            } catch (Exception e) {
                throw new ArithmeticException();
            }
        }
    }

    private static double convertDurationToDoubleInSeconds(Duration duration) {
        long secondsComponent = duration.getSeconds();
        int nanosecondsComponent = duration.getNano();
        return ((double) secondsComponent) + ((double) nanosecondsComponent * 1e-9);
    }


}
