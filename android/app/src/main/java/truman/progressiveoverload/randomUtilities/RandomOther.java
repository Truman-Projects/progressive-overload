package truman.progressiveoverload.randomUtilities;

import java.util.ArrayList;
import java.util.Arrays;

public class RandomOther<Type> {
    I_RandomValueGenerator<Type> generator_;

    public RandomOther(I_RandomValueGenerator<Type> generator) {
        generator_ = generator;
    }

    public Type otherThan(ArrayList<Type> others) {
        Type randomValue = generator_.generate();
        while (others.contains(randomValue)) {
            randomValue = generator_.generate();
        }
        return randomValue;
    }

    public Type otherThan(Type other) {
        return otherThan(new ArrayList<>(Arrays.asList(other)));
    }
}
