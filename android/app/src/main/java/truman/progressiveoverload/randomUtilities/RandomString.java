package truman.progressiveoverload.randomUtilities;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RandomString implements I_RandomValueGenerator<String> {
    private static final int DEFAULT_STRING_LENGTH = 10;

    public String generate(int length) {
        // adapted from https://www.baeldung.com/java-random-string
        byte[] array = new byte[length];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public String generate() {
        return generate(DEFAULT_STRING_LENGTH);
    }

    public String generateAlphanumeric(int length) {
        // adapted from https://www.baeldung.com/java-random-string
        final int asciiValueForZero = 48;
        final int asciiValueForNine = 57;
        final int asciiValueForUppercaseA = 65;
        final int asciiValueForUppercaseZ = 90;
        final int asciiValueForLowercaseA = 97;
        final int asciiValueForLowercaseZ = 122;
        Random random = new Random();

        return random.ints(asciiValueForZero, asciiValueForLowercaseZ + 1)
                .filter(i -> (i <= asciiValueForNine || i >= asciiValueForUppercaseA) && (i <= asciiValueForUppercaseZ || i >= asciiValueForLowercaseA))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateAlphanumeric() {
        return generateAlphanumeric(DEFAULT_STRING_LENGTH);
    }
}
