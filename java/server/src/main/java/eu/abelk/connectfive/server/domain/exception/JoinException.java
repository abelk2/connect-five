package eu.abelk.connectfive.server.domain.exception;

public class JoinException extends ConnectFiveServerException {
    public JoinException(String message, boolean retryable) {
        super(message, retryable);
    }

    public JoinException(String message, Throwable cause, boolean retryable) {
        super(message, cause, retryable);
    }
}
