package eu.abelk.connectfive.server.domain.exception;

public class StateException extends ConnectFiveServerException {
    public StateException(String message, boolean retryable) {
        super(message, retryable);
    }

    public StateException(String message, Throwable cause, boolean retryable) {
        super(message, cause, retryable);
    }
}
