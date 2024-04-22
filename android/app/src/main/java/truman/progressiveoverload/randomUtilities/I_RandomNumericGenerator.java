package truman.progressiveoverload.randomUtilities;

public interface I_RandomNumericGenerator<Type extends Number> extends I_RandomValueGenerator<Type> {
    Type generate(Type minValue, Type maxValue);
}
