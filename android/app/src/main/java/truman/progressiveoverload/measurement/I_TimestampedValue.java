package truman.progressiveoverload.measurement;

// MUST BE IMMUTABLE (Java moment)
public interface I_TimestampedValue {
    long valueInDefaultUnits();

    long unixTimestampMilliseconds();
}
