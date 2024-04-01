package truman.progressiveoverload.testUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class RandomLocalDateTime {
    public static LocalDateTime generate() {
        // yoinked from stack overflow https://stackoverflow.com/questions/34051291/generate-a-random-localdate-with-java-time
        LocalDateTime start = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);
        long days = ChronoUnit.DAYS.between(start, LocalDateTime.now());
        return start.plusDays(new Random().nextInt((int) days + 1));
    }
}
