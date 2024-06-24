package truman.progressiveoverload.measurement.distance;

import truman.progressiveoverload.measurement.MagnitudeOutOfRangeException;

public class Distance {
    private final long centimeters_;

    public Distance(long centimeters) {
        centimeters_ = centimeters;
    }

    public long toCentimeters() {
        return centimeters_;
    }

    public double toMeters() {
        return (double) centimeters_ * CENTIMETERS_TO_METERS;
    }

    public static Distance fromMeters(double meters) throws MagnitudeOutOfRangeException {
        throwExceptionIfConstructingDistanceOutOfRange(meters, MIN_VALUE_METERS, MAX_VALUE_METERS, "meters");
        double centimeters = meters / CENTIMETERS_TO_METERS;
        return new Distance((long) centimeters);
    }

    public double toKilometers() {
        return (double) centimeters_ * CENTIMETERS_TO_KILOMETERS;
    }

    public static Distance fromKilometers(double kilometers) throws MagnitudeOutOfRangeException {
        throwExceptionIfConstructingDistanceOutOfRange(kilometers, MIN_VALUE_KILOMETERS, MAX_VALUE_KILOMETERS, "kilometers");
        double centimeters = kilometers / CENTIMETERS_TO_KILOMETERS;
        return new Distance((long) centimeters);
    }

    public double toInches() {
        return (double) centimeters_ * CENTIMETERS_TO_INCHES;
    }

    public static Distance fromInches(double inches) throws MagnitudeOutOfRangeException {
        throwExceptionIfConstructingDistanceOutOfRange(inches, MIN_VALUE_INCHES, MAX_VALUE_INCHES, "inches");
        double centimeters = inches / CENTIMETERS_TO_INCHES;
        return new Distance((long) centimeters);
    }

    public double toFeet() {
        return (double) centimeters_ * CENTIMETERS_TO_FEET;
    }

    public static Distance fromFeet(double feet) throws MagnitudeOutOfRangeException {
        throwExceptionIfConstructingDistanceOutOfRange(feet, MIN_VALUE_FEET, MAX_VALUE_FEET, "feet");
        double centimeters = feet / CENTIMETERS_TO_FEET;
        return new Distance((long) centimeters);
    }

    public double toMiles() {
        return (double) centimeters_ * CENTIMETERS_TO_MILES;
    }

    public static Distance fromMiles(double miles) throws MagnitudeOutOfRangeException {
        throwExceptionIfConstructingDistanceOutOfRange(miles, MIN_VALUE_MILES, MAX_VALUE_MILES, "miles");
        double centimeters = miles / CENTIMETERS_TO_MILES;
        return new Distance((long) centimeters);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Distance)) {
            return false;
        }
        return (this.centimeters_ == ((Distance) other).toCentimeters());
    }

    private static void throwExceptionIfConstructingDistanceOutOfRange(double value, double minValue, double maxValue,
                                                                       String constructionDescription) throws MagnitudeOutOfRangeException {
        if (value < minValue || value > maxValue) {
            throw new MagnitudeOutOfRangeException("Attempting to instantiate Distance with too many " + constructionDescription);
        }
    }

    private static final double CENTIMETERS_TO_METERS = 1.0e-2;
    private static final double CENTIMETERS_TO_KILOMETERS = 1.0e-5;
    private static final double CENTIMETERS_TO_INCHES = 3.937007874e-1;
    private static final double CENTIMETERS_TO_FEET = CENTIMETERS_TO_INCHES / 12.0;
    private static final double CENTIMETERS_TO_MILES = 6.2137e-6;

    public static final long MIN_VALUE_CENTIMETERS = Long.MIN_VALUE;
    public static final double MIN_VALUE_METERS = (double) MIN_VALUE_CENTIMETERS * CENTIMETERS_TO_METERS;
    public static final double MIN_VALUE_KILOMETERS = (double) MIN_VALUE_CENTIMETERS * CENTIMETERS_TO_KILOMETERS;
    public static final double MIN_VALUE_INCHES = (double) MIN_VALUE_CENTIMETERS * CENTIMETERS_TO_INCHES;
    public static final double MIN_VALUE_FEET = (double) MIN_VALUE_CENTIMETERS * CENTIMETERS_TO_FEET;
    public static final double MIN_VALUE_MILES = (double) MIN_VALUE_CENTIMETERS * CENTIMETERS_TO_MILES;

    public static final long MAX_VALUE_CENTIMETERS = Long.MAX_VALUE;
    public static final double MAX_VALUE_METERS = (double) MAX_VALUE_CENTIMETERS * CENTIMETERS_TO_METERS;
    public static final double MAX_VALUE_KILOMETERS = (double) MAX_VALUE_CENTIMETERS * CENTIMETERS_TO_KILOMETERS;
    public static final double MAX_VALUE_INCHES = (double) MAX_VALUE_CENTIMETERS * CENTIMETERS_TO_INCHES;
    public static final double MAX_VALUE_FEET = (double) MAX_VALUE_CENTIMETERS * CENTIMETERS_TO_FEET;
    public static final double MAX_VALUE_MILES = (double) MAX_VALUE_CENTIMETERS * CENTIMETERS_TO_MILES;
}
