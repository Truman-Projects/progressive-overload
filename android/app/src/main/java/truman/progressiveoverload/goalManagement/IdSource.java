package truman.progressiveoverload.goalManagement;

import java.util.HashSet;

class IdSource implements I_IdSource {
    private final HashSet<Long> existingIds_ = new HashSet<>();
    private final I_RandomLongGenerator rng_;

    public IdSource(I_RandomLongGenerator rng) {
        rng_ = rng;
    }

    @Override
    public synchronized boolean attemptToReserveId(Long id) {
        return existingIds_.add(id);
    }

    @Override
    public Long nextAvailableId() {
        while (true) {
            Long randomLong = rng_.randomLong();
            if (attemptToReserveId(randomLong)) {
                return randomLong;
            }
        }
    }
}
