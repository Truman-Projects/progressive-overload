package truman.progressiveoverload.measurement.distance;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import truman.progressiveoverload.measurement.MagnitudeOutOfRangeException;
import truman.progressiveoverload.randomUtilities.RandomDouble;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestDistance {
    final static double DOUBLE_PRECISION_AS_FRACTION = 1.0e-15; // ~15.9 decimal places, rounded for safety
    final static double SLIGHTLY_MORE_THAN_ONE = 1.0 + DOUBLE_PRECISION_AS_FRACTION;
    final static double SLIGHTLY_LESS_THAN_ONE = 1.0 - DOUBLE_PRECISION_AS_FRACTION;
    private long patientInCentimeters_;
    private Distance patient_;

    @BeforeEach
    public void resetEverything() {
        patientInCentimeters_ = validRandomCentimeters();
        resetPatient();
    }

    private void resetPatient() {
        patient_ = new Distance(patientInCentimeters_);
    }

    @Test
    void willReturnInCentimeters() {
        long actualCentimeters = patient_.toCentimeters();
        assertEquals(patientInCentimeters_, actualCentimeters);
    }

    @Test
    void willConvertToMeters() {
        double expectedMeters = (double) patientInCentimeters_ * CENTIMETERS_TO_METERS;

        double actualMeters = patient_.toMeters();
        assertEquals(expectedMeters, actualMeters);
    }

    @Test
    void willConvertToKilometers() {
        double expectedKilometers = (double) patientInCentimeters_ * CENTIMETERS_TO_KILOMETERS;

        double actualKilometers = patient_.toKilometers();
        assertEquals(expectedKilometers, actualKilometers);
    }

    @Test
    void willConvertToInches() {
        double expectedInches = (double) patientInCentimeters_ * CENTIMETERS_TO_INCHES;

        double actualInches = patient_.toInches();
        assertEquals(expectedInches, actualInches);
    }

    @Test
    void willConvertToFeet() {
        double expectedFeet = (double) patientInCentimeters_ * CENTIMETERS_TO_FEET;

        double actualFeet = patient_.toFeet();
        assertEquals(expectedFeet, actualFeet);
    }

    @Test
    void willConvertToMiles() {
        double expectedMiles = (double) patientInCentimeters_ * CENTIMETERS_TO_MILES;

        double actualMiles = patient_.toMiles();
        assertEquals(expectedMiles, actualMiles);
    }

    @ParameterizedTest
    @MethodSource("willConvertFromMetersWithinValidRange_data")
    public void willConvertFromMetersWithinValidRange(double meters) {
        failOnException(() -> {
            Distance patient = Distance.fromMeters(meters);

            double metersOut = patient.toMeters();
            assertDoubleFuzzyEquals(meters, metersOut);
        });
    }

    // returns (double meters)
    private static Stream<Arguments> willConvertFromMetersWithinValidRange_data() {
        return doublesWithinValidRange(Distance.MIN_VALUE_METERS, Distance.MAX_VALUE_METERS);
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionAttemptingToConvertFromInvalidMeters_data")
    public void willThrowExceptionAttemptingToConvertFromInvalidMeters(double meters) {
        Assertions.assertThrows(MagnitudeOutOfRangeException.class, () -> Distance.fromMeters(meters), "Attempting to instantiate " +
                "Distance with too " +
                "many meters");
    }

    // returns (double meters)
    private static Stream<Arguments> willThrowExceptionAttemptingToConvertFromInvalidMeters_data() {
        return doublesOutsideValidRange(Distance.MIN_VALUE_METERS, Distance.MAX_VALUE_METERS);
    }

    @ParameterizedTest
    @MethodSource("willConvertFromKilometersWithinValidRange_data")
    public void willConvertFromKilometersWithinValidRange(double kilometers) {
        failOnException(() -> {
            Distance patient = Distance.fromKilometers(kilometers);

            double kilometersOut = patient.toKilometers();
            assertDoubleFuzzyEquals(kilometers, kilometersOut);
        });
    }

    // returns (double kilometers)
    private static Stream<Arguments> willConvertFromKilometersWithinValidRange_data() {
        return doublesWithinValidRange(Distance.MIN_VALUE_KILOMETERS, Distance.MAX_VALUE_KILOMETERS);
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionAttemptingToConvertFromInvalidKilometers_data")
    public void willThrowExceptionAttemptingToConvertFromInvalidKilometers(double kilometers) {
        assertThrows(MagnitudeOutOfRangeException.class, () -> Distance.fromKilometers(kilometers), "Attempting to instantiate Distance " +
                "with too many kilometers");
    }

    // returns (double kilometers)
    private static Stream<Arguments> willThrowExceptionAttemptingToConvertFromInvalidKilometers_data() {
        return doublesOutsideValidRange(Distance.MIN_VALUE_KILOMETERS, Distance.MAX_VALUE_KILOMETERS);
    }

    @ParameterizedTest
    @MethodSource("willConvertFromInchesWithinValidRange_data")
    public void willConvertFromInchesWithinValidRange(double inches) {
        failOnException(() -> {
            Distance patient = Distance.fromInches(inches);

            double inchesOut = patient.toInches();
            assertDoubleFuzzyEquals(inches, inchesOut);
        });
    }

    // returns (double inches)
    private static Stream<Arguments> willConvertFromInchesWithinValidRange_data() {
        return doublesWithinValidRange(Distance.MIN_VALUE_INCHES, Distance.MAX_VALUE_INCHES);
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionAttemptingToConvertFromInvalidInches_data")
    public void willThrowExceptionAttemptingToConvertFromInvalidInches(double inches) {
        assertThrows(MagnitudeOutOfRangeException.class, () -> Distance.fromInches(inches), "Attempting to instantiate Distance " +
                "with too many inches");
    }

    // returns (double inches)
    private static Stream<Arguments> willThrowExceptionAttemptingToConvertFromInvalidInches_data() {
        return doublesOutsideValidRange(Distance.MIN_VALUE_INCHES, Distance.MAX_VALUE_INCHES);
    }

    @ParameterizedTest
    @MethodSource("willConvertFromFeetWithinValidRange_data")
    public void willConvertFromFeetWithinValidRange(double feet) {
        failOnException(() -> {
            Distance patient = Distance.fromFeet(feet);

            double feetOut = patient.toFeet();
            assertDoubleFuzzyEquals(feet, feetOut);
        });
    }

    // returns (double feet)
    private static Stream<Arguments> willConvertFromFeetWithinValidRange_data() {
        return doublesWithinValidRange(Distance.MIN_VALUE_FEET, Distance.MAX_VALUE_FEET);
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionAttemptingToConvertFromInvalidFeet_data")
    public void willThrowExceptionAttemptingToConvertFromInvalidFeet(double feet) {
        assertThrows(MagnitudeOutOfRangeException.class, () -> Distance.fromFeet(feet), "Attempting to instantiate Distance " +
                "with too many feet");
    }

    // returns (double feet)
    private static Stream<Arguments> willThrowExceptionAttemptingToConvertFromInvalidFeet_data() {
        return doublesOutsideValidRange(Distance.MIN_VALUE_FEET, Distance.MAX_VALUE_FEET);
    }

    @ParameterizedTest
    @MethodSource("willConvertFromMilesWithinValidRange_data")
    public void willConvertFromMilesWithinValidRange(double miles) {
        failOnException(() -> {
            Distance patient = Distance.fromMiles(miles);

            double milesOut = patient.toMiles();
            assertDoubleFuzzyEquals(miles, milesOut);
        });
    }

    // returns (double miles)
    private static Stream<Arguments> willConvertFromMilesWithinValidRange_data() {
        return doublesWithinValidRange(Distance.MIN_VALUE_MILES, Distance.MAX_VALUE_MILES);
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionAttemptingToConvertFromInvalidMiles_data")
    public void willThrowExceptionAttemptingToConvertFromInvalidMiles(double miles) {
        assertThrows(MagnitudeOutOfRangeException.class, () -> Distance.fromMiles(miles), "Attempting to instantiate Distance " +
                "with too many miles");
    }

    // returns (double miles)
    private static Stream<Arguments> willThrowExceptionAttemptingToConvertFromInvalidMiles_data() {
        return doublesOutsideValidRange(Distance.MIN_VALUE_MILES, Distance.MAX_VALUE_MILES);
    }

    @ParameterizedTest
    @MethodSource("canMultiplyByScalarWithinRange_data")
    public void canMultiplyByScalarWithinRange(long initialCentimeters) {
        patientInCentimeters_ = initialCentimeters;
        resetPatient();
        double maximumMultiplierBeforeOverload = calculateCurrentFactorAwayFromOverflow();
        double minimumMultiplierBeforeOverload = -1.0 * maximumMultiplierBeforeOverload;
        double randomValidMultiplier = new RandomDouble().generate(minimumMultiplierBeforeOverload, maximumMultiplierBeforeOverload);
        long expectedCentimetersAfterMultiplication = (long) ((double) patientInCentimeters_ * randomValidMultiplier);

        failOnException(() -> {
            Distance distanceAfterMultiplication = patient_.multiplyByScalar(randomValidMultiplier);

            assertEquals(expectedCentimetersAfterMultiplication, distanceAfterMultiplication.toCentimeters(), 1);
        });
    }

    // returns (long initialCentimeters)
    private static Stream<Arguments> canMultiplyByScalarWithinRange_data() {
        long randomPositiveCentimeters = new RandomLong().generate(0L, Long.MAX_VALUE);
        long randomNegativeCentimeters = -1L * randomPositiveCentimeters;
        return Stream.of(
                Arguments.of(randomPositiveCentimeters),
                Arguments.of(randomNegativeCentimeters)
        );
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionAttemptingToMultiplyByScalarOutOfRange_data")
    public void willThrowExceptionAttemptingToMultiplyByScalarOutOfRange(long initialCentimeters, double invalidMultiplier) {
        patientInCentimeters_ = initialCentimeters;
        resetPatient();
        assertThrows(MagnitudeOutOfRangeException.class, () -> patient_.multiplyByScalar(invalidMultiplier));
    }

    // returns (long initialCentimeters, double invalidMultiplier)
    private static Stream<Arguments> willThrowExceptionAttemptingToMultiplyByScalarOutOfRange_data() {
        long initialCentimeters = new RandomLong().generate();
        double factorAwayFromOverflow = (double) Distance.MAX_VALUE_CENTIMETERS / (double) initialCentimeters;
        double multiplierToCauseOverflow = factorAwayFromOverflow * SLIGHTLY_MORE_THAN_ONE;

        return Stream.of(
                Arguments.of(initialCentimeters, multiplierToCauseOverflow),
                Arguments.of(initialCentimeters, (-1) * multiplierToCauseOverflow)
        );
    }

    @Test
    public void canDivideByScalarWithinRange() {
        double randomValidDivisor = randomDivisorThatWillNotCauseOverflow();
        long expectedCentimetersAfterDivision = (long) ((double) patientInCentimeters_ / randomValidDivisor);

        failOnException(() -> {
            Distance distanceAfterDivision = patient_.divideByScalar(randomValidDivisor);

            assertEquals(expectedCentimetersAfterDivision, distanceAfterDivision.toCentimeters(), 1);
        });
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionInsteadOfOverflowingDuringDivision_data")
    public void willThrowExceptionAttemptingToDivideByScalarOutOfRange(long initialCentimeters, double invalidDivisor) {
        patientInCentimeters_ = initialCentimeters;
        resetPatient();
        assertThrows(MagnitudeOutOfRangeException.class, () -> patient_.divideByScalar(invalidDivisor));
    }


    @Test
    public void canDivideByValidDistance() {
        long randomValidDivisor = (long) Math.ceil(randomDivisorThatWillNotCauseOverflow());
        double expectedQuotient = ((double) patientInCentimeters_ / randomValidDivisor);
        Distance divisor = new Distance(randomValidDivisor);
        failOnException(() -> {
            double actualQuotient = patient_.divideByDistance(divisor);

            assertEquals(expectedQuotient, actualQuotient);
        });
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionInsteadOfOverflowingDuringDivision_data")
    public void willThrowExceptionAttemptingToDivideByInvalidDistance(long initialCentimeters, double invalidDivisor) {
        patientInCentimeters_ = initialCentimeters;
        resetPatient();
        Distance divisor = new Distance((long) invalidDivisor);
        assertThrows(MagnitudeOutOfRangeException.class, () -> patient_.divideByDistance(divisor));
    }

    // returns (long initialCentimeters, double invalidDivisor)
    private static Stream<Arguments> willThrowExceptionInsteadOfOverflowingDuringDivision_data() {
        long initialCentimeters = new RandomLong().generate();
        double factorAwayFromOverflow = (double) Distance.MAX_VALUE_CENTIMETERS / (double) initialCentimeters;
        double divisorAwayFromOverflow = 1.0 / factorAwayFromOverflow;
        double divisorToCauseOverflow = divisorAwayFromOverflow * SLIGHTLY_LESS_THAN_ONE;

        return Stream.of(
                Arguments.of(initialCentimeters, divisorToCauseOverflow),
                Arguments.of(initialCentimeters, (-1) * divisorToCauseOverflow)
        );
    }

    @ParameterizedTest
    @MethodSource("testEqualityOperator_data")
    public void testEqualityOperator(long centimeters1, long centimeters2, boolean distancesExpectedToBeEqual) {
        Distance distance1 = new Distance(centimeters1);
        Distance distance2 = new Distance(centimeters2);

        assertEquals(distancesExpectedToBeEqual, distance1.equals(distance2));
    }

    //returns (long centimeters1, long centimeters2, boolean distancesExpectedToBeEqual)
    private static Stream<Arguments> testEqualityOperator_data() {
        long randomCentimeters1 = validRandomCentimeters();
        long randomCentimeters2 = validRandomCentimeters();
        boolean distancesNotEqual = false;
        boolean distanceEqual = true;
        return Stream.of(
                Arguments.of(randomCentimeters1, randomCentimeters2, distancesNotEqual),
                Arguments.of(randomCentimeters1, randomCentimeters1, distanceEqual)
        );
    }

    private static long validRandomCentimeters() {
        return new RandomLong().generate(Distance.MIN_VALUE_CENTIMETERS, Distance.MAX_VALUE_CENTIMETERS);
    }

    private void assertDoubleFuzzyEquals(double expected, double actual) {
        assertEquals(expected, actual, Math.abs(expected) * DOUBLE_PRECISION_AS_FRACTION);
    }

    // java requires a functional interface to pass lambdas by argument
    private interface LambdaWithException {
        void fun() throws Exception;
    }

    private void failOnException(LambdaWithException lambda) {
        try {
            lambda.fun();
        } catch (Exception e) {
            fail("Caught unexpected exception" + e.getMessage());
        }
    }

    private double calculateCurrentFactorAwayFromOverflow() {
        long currentAbsoluteValueCentimeters = Math.abs(patientInCentimeters_);
        return (double) Distance.MAX_VALUE_CENTIMETERS / (double) currentAbsoluteValueCentimeters;
    }

    private double randomDivisorThatWillNotCauseOverflow() {
        double minimumDivisorBeforeOverflow = 1.0 / calculateCurrentFactorAwayFromOverflow();
        // setting a tighter upper bound.  Otherwise result will most often just be ~0, which does not provide meaningful coverage
        return new RandomDouble().generate(minimumDivisorBeforeOverflow,
                Math.min(minimumDivisorBeforeOverflow * 2.0, Double.MAX_VALUE));
    }

    private static Stream<Arguments> doublesWithinValidRange(double minValue, double maxValue) {
        double randomValueWithinRange = new RandomDouble().generate(minValue, maxValue);
        return Stream.of(
                Arguments.of(minValue),
                Arguments.of(randomValueWithinRange),
                Arguments.of(maxValue)
        );
    }

    private static Stream<Arguments> doublesOutsideValidRange(double minValue, double maxValue) {
        return Stream.of(
                Arguments.of(minValue * SLIGHTLY_MORE_THAN_ONE),
                Arguments.of(maxValue * SLIGHTLY_MORE_THAN_ONE)
        );
    }

    private final double CENTIMETERS_TO_METERS = (double) 1e-2;
    private final double CENTIMETERS_TO_KILOMETERS = (double) 1e-5;
    private final double CENTIMETERS_TO_INCHES = 3.937007874e-1;
    private final double CENTIMETERS_TO_FEET = CENTIMETERS_TO_INCHES / 12.0;
    private final double CENTIMETERS_TO_MILES = 6.2137e-6;


}