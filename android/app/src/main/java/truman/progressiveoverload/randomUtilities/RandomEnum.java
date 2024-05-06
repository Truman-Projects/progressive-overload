package truman.progressiveoverload.randomUtilities;

public class RandomEnum<EnumType extends Enum<EnumType>> implements I_RandomValueGenerator<EnumType> {
    private final EnumType[] enumValues_;

    public RandomEnum(Class<EnumType> enumClass) throws NullPointerException {
        enumValues_ = enumClass.getEnumConstants();
        if (enumValues_ == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public EnumType generate() {
        int randomIndex = new RandomInt().generate(0, enumValues_.length);
        return enumValues_[randomIndex];
    }
}
