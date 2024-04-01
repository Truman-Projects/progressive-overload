package truman.progressiveoverload.goalManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import truman.progressiveoverload.measurement.Mass;
import truman.progressiveoverload.testUtils.RandomLocalDateTime;

class TestTimestampedMass {
    @Test
    public void willProvideValueInDefaultUnitsOfMilligrams() {
        long randomMilligrams = ThreadLocalRandom.current().nextLong();
        LocalDateTime arbitraryTimestamp = RandomLocalDateTime.generate();
        Mass mass = new Mass(randomMilligrams);

        TimestampedMass timestampedMass = new TimestampedMass(mass, arbitraryTimestamp);

        assertEquals(randomMilligrams, timestampedMass.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        long arbitraryMilligrams = ThreadLocalRandom.current().nextLong();
        LocalDateTime randomTimestamp = RandomLocalDateTime.generate();
        Mass mass = new Mass(arbitraryMilligrams);

        TimestampedMass timestampedMass = new TimestampedMass(mass, randomTimestamp);

        assertEquals(randomTimestamp, timestampedMass.timestamp());
    }

    @Test
    public void willReturnCorrectMassObject() {
        long randomMilligrams = ThreadLocalRandom.current().nextLong();
        LocalDateTime randomTimestamp = RandomLocalDateTime.generate();
        Mass expectedMass = new Mass(randomMilligrams);

        TimestampedMass timestampedMass = new TimestampedMass(expectedMass, randomTimestamp);

        assertEquals(expectedMass, timestampedMass.mass());
    }


}