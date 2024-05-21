package truman.progressiveoverload.goalManagement.api;

public class InvalidQueryException extends Exception {
    public InvalidQueryException(String errorMessage) {
        super(errorMessage);
    }
}
