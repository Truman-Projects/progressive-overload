package truman.progressiveoverload.goalManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import truman.progressiveoverload.measurement.Mass;
import truman.progressiveoverload.randomUtilities.RandomLocalDateTime;
import truman.progressiveoverload.randomUtilities.RandomLong;

class TestTimestampedMass {
    @Test
    public void willProvideValueInDefaultUnitsOfMilligrams() {
        long randomMilligrams = new RandomLong().generate();
        LocalDateTime arbitraryTimestamp = new RandomLocalDateTime().generate();
        Mass mass = new Mass(randomMilligrams);

        TimestampedMass timestampedMass = new TimestampedMass(mass, arbitraryTimestamp);

        assertEquals(randomMilligrams, timestampedMass.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        long arbitraryMilligrams = new RandomLong().generate();
        LocalDateTime randomTimestamp = new RandomLocalDateTime().generate();
        Mass mass = new Mass(arbitraryMilligrams);

        TimestampedMass timestampedMass = new TimestampedMass(mass, randomTimestamp);

        assertEquals(randomTimestamp, timestampedMass.timestamp());
    }

    @Test
    public void willReturnCorrectMassObject() {
        long randomMilligrams = new RandomLong().generate();
        LocalDateTime randomTimestamp = new RandomLocalDateTime().generate();
        Mass expectedMass = new Mass(randomMilligrams);

        TimestampedMass timestampedMass = new TimestampedMass(expectedMass, randomTimestamp);

        assertEquals(expectedMass, timestampedMass.mass());
    }


}