package truman.progressiveoverload.goalManagement;

import java.util.concurrent.ThreadLocalRandom;

class RandomLongGenerator implements I_RandomLongGenerator {
    @Override
    public Long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }
}
