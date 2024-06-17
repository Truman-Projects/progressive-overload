package truman.progressiveoverload.goalManagement.api;

public class DuplicateEntryException extends Exception {
    public DuplicateEntryException(String errorMessage) {
        super(errorMessage);
    }
}
