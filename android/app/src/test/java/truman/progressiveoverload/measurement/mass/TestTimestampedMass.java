package truman.progressiveoverload.measurement.mass;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import truman.progressiveoverload.randomUtilities.RandomLong;

class TestTimestampedMass {
    @Test
    public void willProvideValueInDefaultUnitsOfMilligrams() {
        Mass mass = new RandomMass().generate();
        long unixTimestampMilliseconds = new RandomLong().generate();

        TimestampedMass timestampedMass = new TimestampedMass(mass, unixTimestampMilliseconds);

        assertEquals(mass.toMilligrams(), timestampedMass.valueInDefaultUnits());
    }

    @Test
    public void willReturnCorrectTimestamp() {
        Mass mass = new RandomMass().generate();
        long unixTimestampMilliseconds = new RandomLong().generate();

        TimestampedMass timestampedMass = new TimestampedMass(mass, unixTimestampMilliseconds);

        assertEquals(unixTimestampMilliseconds, timestampedMass.unixTimestampMilliseconds());
    }

    @Test
    public void willReturnCorrectMassObject() {
        long randomTimestamp = new RandomLong().generate();
        Mass expectedMass = new RandomMass().generate();

        TimestampedMass timestampedMass = new TimestampedMass(expectedMass, randomTimestamp);

        assertEquals(expectedMass, timestampedMass.mass());
    }


}