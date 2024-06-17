package truman.progressiveoverload.randomUtilities;

import java.util.ArrayList;
import java.util.Set;

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

    public Type otherThan(Set<Type> others) {
        return otherThan(new ArrayList<>(others));
    }

    public Type otherThan(Type other) {
        ArrayList<Type> arrayList = new ArrayList<>();
        arrayList.add(other);
        return otherThan(arrayList);
    }
}
