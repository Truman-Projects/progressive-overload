package truman.progressiveoverload.randomUtilities;

import java.util.ArrayList;

public class RandomArrayList<Type> implements I_RandomValueGenerator<ArrayList<Type>> {

    private static final int DEFAULT_SIZE = 3;
    private final I_RandomValueGenerator<Type> generator_;

    public RandomArrayList(I_RandomValueGenerator<Type> generator) {
        generator_ = generator;
    }

    public ArrayList<Type> generate(int size) {
        ArrayList<Type> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Type element = generator_.generate();
            list.add(element);
        }
        return list;
    }

    public ArrayList<Type> generate() {
        return generate(DEFAULT_SIZE);
    }

}
