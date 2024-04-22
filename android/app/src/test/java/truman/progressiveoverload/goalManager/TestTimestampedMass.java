package truman.progressiveoverload.goalManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import truman.progressiveoverload.measurement.Mass;
import truman.progressiveoverload.measurement.RandomMass;
import truman.progressiveoverload.randomUtilities.RandomLocalDateTime;

class TestTimestampedMass {
    @Test
    public void willProvideValueInDefaultUnitsOfMilligrams() {
        Mass mass = new RandomMass().generate();
        LocalDateTime arbitraryTimestamp = new RandomLocalDateTime().generate();

        TimestampedMass timestampedMass = new TimestampedMass(mass, arbitraryTimestamp);

        assertEquals(mass.toMilligrams(), timestampedMass.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        Mass mass = new RandomMass().generate();
        LocalDateTime randomTimestamp = new RandomLocalDateTime().generate();

        TimestampedMass timestampedMass = new TimestampedMass(mass, randomTimestamp);

        assertEquals(randomTimestamp, timestampedMass.timestamp());
    }

    @Test
    public void willReturnCorrectMassObject() {
        LocalDateTime randomTimestamp = new RandomLocalDateTime().generate();
        Mass expectedMass = new RandomMass().generate();

        TimestampedMass timestampedMass = new TimestampedMass(expectedMass, randomTimestamp);

        assertEquals(expectedMass, timestampedMass.mass());
    }


}