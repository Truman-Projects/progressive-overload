package truman.progressiveoverload.measurement;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class TestMass {

    @Test
    public void willReturnInMilligrams() {
        long expectedMilligrams = ThreadLocalRandom.current().nextLong();
        Mass patient = new Mass(expectedMilligrams);

        long actualMilligrams = patient.toMilligrams();
        assertEquals(expectedMilligrams, actualMilligrams);
    }

    @Test
    public void willConvertToGrams() {
        final double MILLIGRAMS_TO_GRAMS = 1.0 / 1000.0;
        long randomMilligrams = ThreadLocalRandom.current().nextLong();

        double expectedGrams = (double) randomMilligrams * MILLIGRAMS_TO_GRAMS;
        Mass patient = new Mass(randomMilligrams);

        double actualGrams = patient.toGrams();
        assertEquals(expectedGrams, actualGrams, 1e-7);
    }

    @Test
    public void willConvertToKilograms() {
        final double MILLIGRAMS_TO_KILOS = 1.0 / 1000000.0;
        long randomMilligrams = ThreadLocalRandom.current().nextLong();

        double expectedKilos = (double) randomMilligrams * MILLIGRAMS_TO_KILOS;
        Mass patient = new Mass(randomMilligrams);

        double actualKilos = patient.toKilograms();
        assertEquals(expectedKilos, actualKilos, 1e-7);
    }

    @Test
    public void willConvertToPounds() {
        final double MILLIGRAMS_TO_POUNDS = 2.20462e-6;
        long randomMilligrams = ThreadLocalRandom.current().nextLong();

        double expectedPounds = (double) randomMilligrams * MILLIGRAMS_TO_POUNDS;
        Mass patient = new Mass(randomMilligrams);

        double actualPounds = patient.toPounds();
        assertEquals(expectedPounds, actualPounds, 1e-3);
    }

    @Test
    public void willConvertFromGrams() {
        double randomGramsIn = ThreadLocalRandom.current().nextDouble();

        Mass patient = Mass.fromGrams(randomGramsIn);

        double gramsOut = patient.toGrams();
        assertEquals(randomGramsIn, gramsOut, 1e-3);
    }

    @Test
    public void willConvertFromKilograms() {
        double randomKilosIn = ThreadLocalRandom.current().nextDouble();

        Mass patient = Mass.fromKilograms(randomKilosIn);

        double kilosOut = patient.toKilograms();
        assertEquals(randomKilosIn, kilosOut, 1e-3);
    }

    @Test
    public void willConvertFromPounds() {
        double randomPoundsIn = ThreadLocalRandom.current().nextDouble();

        Mass patient = Mass.fromPounds(randomPoundsIn);

        double poundsOut = patient.toPounds();
        assertEquals(randomPoundsIn, poundsOut, 1e-3);
    }

    @Test
    public void canAddTwoMasses() {
        long halfMaxLong = (long) (Long.MAX_VALUE / 2);
        long randomMilligrams1 = ThreadLocalRandom.current().nextLong(halfMaxLong);
        long randomMilligrams2 = ThreadLocalRandom.current().nextLong(halfMaxLong);
        long milligrams1Plus2 = randomMilligrams1 + randomMilligrams2;
        Mass mass1 = new Mass(randomMilligrams1);
        Mass mass2 = new Mass(randomMilligrams2);
        Mass mass1PlusMass2 = mass1.plus(mass2);

        assertEquals(milligrams1Plus2, mass1PlusMass2.toMilligrams());

    }

    @Test
    public void canSubtractTwoMasses() {
        long randomMilligrams1 = ThreadLocalRandom.current().nextLong();
        long randomMilligrams2 = ThreadLocalRandom.current().nextLong();
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

        assertEquals((mass1.equals(mass2)), massesExpectedToBeEqual);
    }

    private static Stream<Arguments> testEqualityOperator_data() {
        long randomMilligrams1 = ThreadLocalRandom.current().nextLong();
        long randomMilligrams2 = ThreadLocalRandom.current().nextLong(); // ThreadLocalRandom guarantees different value
        boolean massesNotEqual = false;
        boolean massesEqual = true;
        return Stream.of(
                Arguments.of(randomMilligrams1, randomMilligrams2, massesNotEqual),
                Arguments.of(randomMilligrams1, randomMilligrams1, massesEqual)
        );
    }

}