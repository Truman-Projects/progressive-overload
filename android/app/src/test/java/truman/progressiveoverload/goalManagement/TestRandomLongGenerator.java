package truman.progressiveoverload.goalManagement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class TestRandomLongGenerator {
    private RandomLongGenerator patient_;

    @BeforeEach
    public void resetEverything() {
        patient_ = new RandomLongGenerator();
    }

    @Test
    public void willGenerateDifferentNumbersAtLeastEightOfTenTimes() {
        // My best shot at testing that it uses Java's RNG under the hood
        final int NUMBER_OF_LONGS_TO_GENERATE = 10;
        final int MINIMUM_NUMBER_UNIQUE_LONGS = 8;
        HashSet<Long> uniqueLongs = new HashSet<>();

        for (int i = 0; i < NUMBER_OF_LONGS_TO_GENERATE; i++) {
            uniqueLongs.add(patient_.randomLong());
        }

        assertTrue(uniqueLongs.size() >= MINIMUM_NUMBER_UNIQUE_LONGS);
    }

}