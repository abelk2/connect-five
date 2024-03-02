package eu.abelk.connectfive.server.domain.exception;

public class StepException extends ConnectFiveServerException {
    public StepException(String message, boolean retryable) {
        super(message, retryable);
    }

    public StepException(String message, Throwable cause, boolean retryable) {
        super(message, cause, retryable);
    }
}
