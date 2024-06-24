package truman.progressiveoverload.measurement.mass;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import truman.progressiveoverload.measurement.MagnitudeOutOfRangeException;
import truman.progressiveoverload.randomUtilities.RandomDouble;
import truman.progressiveoverload.randomUtilities.RandomLong;

public class TestMass {

    @Test
    public void willReturnInMilligrams() {
        long expectedMilligrams = new RandomLong().generate(Mass.MIN_VALUE_MILLIGRAMS, Mass.MAX_VALUE_MILLIGRAMS);
        Mass patient = new Mass(expectedMilligrams);

        long actualMilligrams = patient.toMilligrams();
        assertEquals(expectedMilligrams, actualMilligrams);
    }

    @Test
    public void willConvertToGrams() {
        long randomMilligrams = validRandomMilligrams();

        double expectedGrams = (double) randomMilligrams * MILLIGRAMS_TO_GRAMS;
        Mass patient = new Mass(randomMilligrams);

        double actualGrams = patient.toGrams();
        assertDoubleFuzzyEquals(expectedGrams, actualGrams);
    }

    @Test
    public void willConvertToKilograms() {
        long randomMilligrams = validRandomMilligrams();

        double expectedKilos = (double) randomMilligrams * MILLIGRAMS_TO_KILOGRAMS;
        Mass patient = new Mass(randomMilligrams);

        double actualKilos = patient.toKilograms();
        assertDoubleFuzzyEquals(expectedKilos, actualKilos);
    }

    @Test
    public void willConvertToPounds() {
        long randomMilligrams = validRandomMilligrams();

        double expectedPounds = (double) randomMilligrams * MILLIGRAMS_TO_POUNDS;
        Mass patient = new Mass(randomMilligrams);

        double actualPounds = patient.toPounds();
        assertDoubleFuzzyEquals(expectedPounds, actualPounds);
    }

    @ParameterizedTest
    @MethodSource("willConvertFromGramsWithinValidRange_data")
    public void willConvertFromGramsWithinValidRange(double grams) {
        try {
            Mass patient = Mass.fromGrams(grams);

            double gramsOut = patient.toGrams();
            assertDoubleFuzzyEquals(grams, gramsOut);
        } catch (MagnitudeOutOfRangeException exception) {
            fail("Unexpected exception caught");
        }
    }

    // returns (double grams)
    private static Stream<Arguments> willConvertFromGramsWithinValidRange_data() {
        double leastPossibleGramsAllowed = Mass.MIN_VALUE_GRAMS;
        double randomValidGrams = new RandomDouble().generate(Mass.MIN_VALUE_GRAMS + 1, Mass.MAX_VALUE_GRAMS - 1);
        double greatestPossibleGramsAllowed = Mass.MAX_VALUE_GRAMS;
        return Stream.of(
                Arguments.of(leastPossibleGramsAllowed),
                Arguments.of(randomValidGrams),
                Arguments.of(greatestPossibleGramsAllowed)
        );
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionAttemptingToConvertFromInvalidGrams_data")
    public void willThrowExceptionAttemptingToConvertFromInvalidGrams(double grams) {
        assertThrows(MagnitudeOutOfRangeException.class, () -> Mass.fromGrams(grams));
    }

    // returns (double grams)
    private static Stream<Arguments> willThrowExceptionAttemptingToConvertFromInvalidGrams_data() {
        double gramsLessThanAllowed = Mass.MIN_VALUE_GRAMS - 1.01; // 1.01 because doubles get rounded weird
        double gramsMoreThanAllowed = Mass.MAX_VALUE_GRAMS + 1.01;
        return Stream.of(
                Arguments.of(gramsLessThanAllowed),
                Arguments.of(gramsMoreThanAllowed)
        );
    }


    @ParameterizedTest
    @MethodSource("willConvertFromKilogramsWithinValidRange_data")
    public void willConvertFromKilogramsWithinValidRange(double kilograms) {
        try {
            Mass patient = Mass.fromKilograms(kilograms);

            double kilosOut = patient.toKilograms();
            assertDoubleFuzzyEquals(kilograms, kilosOut);
        } catch (MagnitudeOutOfRangeException exception) {
            fail("Unexpected exception caught");
        }
    }

    // returns (double kilograms)
    private static Stream<Arguments> willConvertFromKilogramsWithinValidRange_data() {
        double leastPossibleKilosAllowed = Mass.MIN_VALUE_KILOGRAMS;
        double randomValidKilos = new RandomDouble().generate(Mass.MIN_VALUE_KILOGRAMS + 1, Mass.MAX_VALUE_KILOGRAMS - 1);
        double greatestPossibleKilosAllowed = Mass.MAX_VALUE_KILOGRAMS;
        return Stream.of(
                Arguments.of(leastPossibleKilosAllowed),
                Arguments.of(randomValidKilos),
                Arguments.of(greatestPossibleKilosAllowed)
        );
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionAttemptingToConvertFromInvalidKilograms_data")
    public void willThrowExceptionAttemptingToConvertFromInvalidKilograms(double kilograms) {
        assertThrows(MagnitudeOutOfRangeException.class, () -> Mass.fromKilograms(kilograms));
    }

    // returns (double kilograms)
    private static Stream<Arguments> willThrowExceptionAttemptingToConvertFromInvalidKilograms_data() {
        double kilogramsLessThanAllowed = Mass.MIN_VALUE_KILOGRAMS - 1.0;
        double kilogramsMoreThanAllowed = Mass.MAX_VALUE_KILOGRAMS + 1.0;
        return Stream.of(
                Arguments.of(kilogramsLessThanAllowed),
                Arguments.of(kilogramsMoreThanAllowed)
        );
    }

    @ParameterizedTest
    @MethodSource("willConvertFromPoundsWithinValidRange_data")
    public void willConvertFromPoundsWithinValidRange(double pounds) {
        try {
            Mass patient = Mass.fromPounds(pounds);

            double poundsOut = patient.toPounds();
            assertDoubleFuzzyEquals(pounds, poundsOut);
        } catch (MagnitudeOutOfRangeException exception) {
            fail("Unexpected exception caught");
        }
    }

    // returns (double pounds)
    private static Stream<Arguments> willConvertFromPoundsWithinValidRange_data() {
        double leastPossiblePoundsAllowed = Mass.MIN_VALUE_POUNDS;
        double randomValidPounds = new RandomDouble().generate(Mass.MIN_VALUE_POUNDS + 1.01, Mass.MAX_VALUE_POUNDS - 1.01);
        double greatestPossiblePoundsAllowed = Mass.MAX_VALUE_POUNDS;

        return Stream.of(
                Arguments.of(leastPossiblePoundsAllowed),
                Arguments.of(randomValidPounds),
                Arguments.of(greatestPossiblePoundsAllowed)
        );
    }

    @ParameterizedTest
    @MethodSource("willThrowArgumentAttemptingToConvertFromInvalidPounds_data")
    public void willThrowArgumentAttemptingToConvertFromInvalidPounds(double pounds) {
        assertThrows(MagnitudeOutOfRangeException.class, () -> Mass.fromPounds(pounds));
    }

    // returns (double pounds)
    private static Stream<Arguments> willThrowArgumentAttemptingToConvertFromInvalidPounds_data() {
        double poundsLessThanAllowed = Mass.MIN_VALUE_POUNDS - 1.0;
        double poundsMoreThanAllowed = Mass.MAX_VALUE_POUNDS + 1.0;
        return Stream.of(
                Arguments.of(poundsLessThanAllowed),
                Arguments.of(poundsMoreThanAllowed)
        );
    }

    @Test
    public void canAddTwoMasses() {
        long halfMaxMilligrams = (long) (Mass.MAX_VALUE_MILLIGRAMS / 2);
        long halfMinMilligrams = (long) (Mass.MIN_VALUE_MILLIGRAMS / 2);
        long randomMilligrams1 = new RandomLong().generate(halfMinMilligrams, halfMaxMilligrams);
        long randomMilligrams2 = new RandomLong().generate(halfMinMilligrams, halfMaxMilligrams);
        long milligrams1Plus2 = randomMilligrams1 + randomMilligrams2;
        Mass mass1 = new Mass(randomMilligrams1);
        Mass mass2 = new Mass(randomMilligrams2);

        try {
            Mass mass1PlusMass2 = mass1.plus(mass2);

            assertEquals(milligrams1Plus2, mass1PlusMass2.toMilligrams());
        } catch (MagnitudeOutOfRangeException exception) {
            fail("Caught unexpected exception");
        }
    }

    @ParameterizedTest
    @MethodSource("willThrowExceptionInsteadOfOverflowingWhenAddingTwoMasses_data")
    public void willThrowExceptionInsteadOfOverflowingWhenAddingTwoMasses(Mass mass1, Mass mass2) {
        assertThrows(MagnitudeOutOfRangeException.class, () -> mass1.plus(mass2));
    }

    // returns (Mass mass1, Mass mass2)
    private static Stream<Arguments> willThrowExceptionInsteadOfOverflowingWhenAddingTwoMasses_data() {
        Mass maxMass = new Mass(Mass.MAX_VALUE_MILLIGRAMS);
        Mass smallPositiveMass = new Mass(1);
        Mass minMass = new Mass(Mass.MIN_VALUE_MILLIGRAMS);
        Mass smallNegativeMass = new Mass(-1);

        return Stream.of(
                Arguments.of(maxMass, smallPositiveMass), // overflow
                Arguments.of(minMass, smallNegativeMass) // underflow
        );
    }

    @Test
    public void canSubtractTwoMasses() {
        long randomMilligrams1 = validRandomMilligrams();
        long randomMilligrams2 = validRandomMilligrams();
        long milligrams1Minus2 = randomMilligrams1 - randomMilligrams2;

        Mass mass1 = new Mass(randomMilligrams1);
        Mass mass2 = new Mass(randomMilligrams2);
        Mass mass1MinusMass2 = mass1.minus(mass2);

        assertEquals(milligrams1Minus2, mass1MinusMass2.toMilligrams());

    }

    @ParameterizedTest
    @MethodSource("testEqualityOperator_data")
    public void testEqualityOperator(long milligrams1, long milligrams2, boolean massesExpectedToBeEqual) {
        Mass mass1 = new Mass(milligrams1);
        Mass mass2 = new Mass(milligrams2);

        assertEquals(massesExpectedToBeEqual, (mass1.equals(mass2)));
    }

    private static Stream<Arguments> testEqualityOperator_data() {
        long randomMilligrams1 = validRandomMilligrams();
        long randomMilligrams2 = validRandomMilligrams();
        boolean massesNotEqual = false;
        boolean massesEqual = true;
        return Stream.of(
                Arguments.of(randomMilligrams1, randomMilligrams2, massesNotEqual),
                Arguments.of(randomMilligrams1, randomMilligrams1, massesEqual)
        );
    }

    private void assertDoubleFuzzyEquals(double expected, double actual) {
        assertEquals(expected, actual, Math.abs(expected) * 1.0E-16);
    }

    private static long validRandomMilligrams() {
        return new RandomLong().generate(Mass.MIN_VALUE_MILLIGRAMS, Mass.MAX_VALUE_MILLIGRAMS);
    }

    private final double MILLIGRAMS_TO_GRAMS = (double) 1e-3;
    private final double MILLIGRAMS_TO_KILOGRAMS = (double) 1e-6;
    private final double MILLIGRAMS_TO_POUNDS = 2.20462e-6;

}