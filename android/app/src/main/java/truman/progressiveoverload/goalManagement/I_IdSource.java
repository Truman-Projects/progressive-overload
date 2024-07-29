package truman.progressiveoverload.goalManagement;

interface I_IdSource {
    boolean attemptToReserveId(Long id);

    Long nextAvailableId();
}
