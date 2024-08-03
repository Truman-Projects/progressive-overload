package truman.progressiveoverload.goalManagement;

import java.util.HashSet;

// No interface for this class because it's role is to enforce uniqueness of ID's,
// which is not something that can be abstracted away
class UniqueIdSource {
    private final HashSet<Long> existingIds_ = new HashSet<>();
    private final I_RandomLongGenerator rng_;

    public UniqueIdSource(I_RandomLongGenerator rng) {
        rng_ = rng;
    }

    public synchronized boolean attemptToReserveId(Long id) {
        return existingIds_.add(id);
    }

    public Long nextAvailableId() {
        while (true) {
            Long randomLong = rng_.randomLong();
            if (attemptToReserveId(randomLong)) {
                return randomLong;
            }
        }
    }
}
