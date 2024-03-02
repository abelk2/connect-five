package eu.abelk.connectfive.server.domain.exception;

public class DisconnectException extends ConnectFiveServerException {
    public DisconnectException(String message, boolean retryable) {
        super(message, retryable);
    }

    public DisconnectException(String message, Throwable cause, boolean retryable) {
        super(message, cause, retryable);
    }
}
