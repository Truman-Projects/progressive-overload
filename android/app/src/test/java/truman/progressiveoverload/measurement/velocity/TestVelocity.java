package truman.progressiveoverload.measurement.velocity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.Stream;

import truman.progressiveoverload.measurement.distance.Distance;
import truman.progressiveoverload.measurement.distance.RandomDistance;
import truman.progressiveoverload.randomUtilities.RandomLong;
import truman.progressiveoverload.randomUtilities.RandomOther;

class TestVelocity {
    private long patientInCentimetersPerHour_;
    private Distance distancePerHour_;
    private Velocity patient_;

    private final static double NANOSECONDS_PER_HOUR = 60 * 60 * 1e9;

    @BeforeEach
    public void resetEverything() {
        patientInCentimetersPerHour_ = new RandomOther<>(new RandomLong()).otherThan(0L);
        resetPatient();
    }

    private void resetPatient() {
        distancePerHour_ = Distance.fromCentimeters(patientInCentimetersPerHour_);
        patient_ = Velocity.fromDistancePerHour(distancePerHour_);
    }

    @Test
    public void willReturnDistancePerHour() {
        Distance actualDistancePerHour = patient_.toDistancePerHour();

        assertEquals(distancePerHour_, actualDistancePerHour);
    }

    @ParameterizedTest
    @MethodSource("willConvertToTimePerKilometer_data")
    public void willConvertToTimePerKilometer(long centimetersPerHour, long expectedNanosecondsPerKilometer) {
        Duration expectedDurationPerKilometer = Duration.ofNanos(expectedNanosecondsPerKilometer);
        patientInCentimetersPerHour_ = centimetersPerHour;
        resetPatient();

        Duration actualDurationPerKilometer = patient_.toTimePerKilometer();

        assertEquals(expectedDurationPerKilometer, actualDurationPerKilometer);
    }

    // returns (long centimetersPerHour, long expectedNanosecondsPerKilometer)
    private static Stream<Arguments> willConvertToTimePerKilometer_data() {
        final double CENTIMETERS_PER_KILOMETER = 1e5;
        final long ONE_NANOSEC_PER_KM_IN_CM_PER_HOUR = (long) (NANOSECONDS_PER_HOUR * CENTIMETERS_PER_KILOMETER);
        return inverseRelationshipTestCases(ONE_NANOSEC_PER_KM_IN_CM_PER_HOUR);
    }

    @ParameterizedTest
    @MethodSource("willConvertFromTimePerKilometer_data")
    public void willConvertFromTimePerKilometer(long nanosecondsPerKilometer, long expectedCmPerHour) {
        Duration timePerKilometer = Duration.ofNanos(nanosecondsPerKilometer);

        Velocity patient = Velocity.fromTimePerKilometer(timePerKilometer);

        assertEquals(expectedCmPerHour, patient.toDistancePerHour().toCentimeters());
    }

    //returns (long nanosecondsPerKilometer, long expectedCmPerHour)
    private static Stream<Arguments> willConvertFromTimePerKilometer_data() {
        final double CENTIMETERS_PER_KILOMETER = 1e5;
        final long ONE_CM_PER_HOUR_IN_NANOSECS_PER_KM = (long) (NANOSECONDS_PER_HOUR * CENTIMETERS_PER_KILOMETER);

        return inverseRelationshipTestCases(ONE_CM_PER_HOUR_IN_NANOSECS_PER_KM);
    }

    @ParameterizedTest
    @MethodSource("willConvertToTimePerMile_data")
    public void willConvertToTimePerMile(long centimetersPerHour, long expectedNanosecondsPerMile) {
        Duration expectedDurationPerMile = Duration.ofNanos(expectedNanosecondsPerMile);
        patientInCentimetersPerHour_ = centimetersPerHour;
        resetPatient();

        Duration actualDurationPerMile = patient_.toTimePerMile();

        assertEquals(expectedDurationPerMile, actualDurationPerMile);
    }

    // returns (long centimetersPerHour, long expectedNanosecondsPerMile)
    private static Stream<Arguments> willConvertToTimePerMile_data() {
        final double MILES_PER_CENTIMETER = 6.2137e-6; // taken from Distance.java for consistency
        final long ONE_NANOSEC_PER_MILE_IN_CM_PER_HOUR = (long) (NANOSECONDS_PER_HOUR / MILES_PER_CENTIMETER);
        return inverseRelationshipTestCases(ONE_NANOSEC_PER_MILE_IN_CM_PER_HOUR);
    }

    @ParameterizedTest
    @MethodSource("willConvertFromTimePerMile_data")
    public void willConvertFromTimePerMile(long nanosecondsPerMile, long expectedCmPerHour) {
        Duration timePerMile = Duration.ofNanos(nanosecondsPerMile);

        Velocity patient = Velocity.fromTimePerMile(timePerMile);

        assertEquals(expectedCmPerHour, patient.toDistancePerHour().toCentimeters());
    }

    // returns (long nanosecondsPerMile, long expectedCmPerHour)
    private static Stream<Arguments> willConvertFromTimePerMile_data() {
        final double MILES_PER_CENTIMETER = 6.2137e-6; // taken from Distance.java for consistency
        final long ONE_CM_PER_HOUR_IN_NANOSECS_PER_MILE = (long) (NANOSECONDS_PER_HOUR / MILES_PER_CENTIMETER);

        return inverseRelationshipTestCases(ONE_CM_PER_HOUR_IN_NANOSECS_PER_MILE);
    }

    @ParameterizedTest
    @MethodSource("testEqualityOperator_data")
    public void testEqualityOperator(Distance distancePerHour1, Distance distancePerHour2, boolean velocitiesExpectedToBeEqual) {
        Velocity velocity1 = Velocity.fromDistancePerHour(distancePerHour1);
        Velocity velocity2 = Velocity.fromDistancePerHour(distancePerHour2);

        assertEquals(velocitiesExpectedToBeEqual, velocity1.equals(velocity2));
    }

    // returns (Distance distancePerHour1, Distance distancePerHour2, boolean velocitiesExpectedToBeEqual)
    private static Stream<Arguments> testEqualityOperator_data() {
        RandomDistance generator = new RandomDistance();
        Distance distancePerHour1 = generator.generate();
        Distance distancePerHour2 = new RandomOther<>(generator).otherThan(distancePerHour1);

        boolean velocitiesNotEqual = false;
        boolean velocitiesEqual = true;

        return Stream.of(
                Arguments.of(distancePerHour1, distancePerHour2, velocitiesNotEqual),
                Arguments.of(distancePerHour1, distancePerHour1, velocitiesEqual)
        );
    }

    @Test
    public void testToString() {
        String expectedStringInCentimetersPerHour = distancePerHour_.toString() + " per hour";
        assertEquals(expectedStringInCentimetersPerHour, patient_.toString());
    }

    // returns (long input, long expectedOutput)
    private static Stream<Arguments> inverseRelationshipTestCases(long inverseRelationshipConversionFactor) {
        RandomLong generator = new RandomLong();

        // input that will not round to zero when converted
        long meaningfulInput = generator.generate(-1 * inverseRelationshipConversionFactor, inverseRelationshipConversionFactor);
        // inputs that will round to zero when converted:
        long badInput_tooNegative = generator.generate(Long.MIN_VALUE, (-1 * inverseRelationshipConversionFactor) - 1);
        long badInput_tooPositive = generator.generate(inverseRelationshipConversionFactor + 1, Long.MAX_VALUE);

        long expectedOutputForMeaningfulInput = (long) ((double) inverseRelationshipConversionFactor / (double) meaningfulInput);

        return Stream.of(
                Arguments.of(meaningfulInput, expectedOutputForMeaningfulInput), // happy case
                Arguments.of(0, 0), // input of zero, expect a sentinel output of zero rather than infinity
                Arguments.of(badInput_tooNegative, 0), // large negative input, expect rounding to zero
                Arguments.of(badInput_tooPositive, 0) //large positive input, expect rounding to zero
        );
    }


}