package eu.abelk.connectfive.server.domain.exception;

import lombok.Getter;

public class ConnectFiveServerException extends RuntimeException {

    @Getter
    private final boolean retryable;

    public ConnectFiveServerException(String message, boolean retryable) {
        super(message);
        this.retryable = retryable;
    }

    public ConnectFiveServerException(String message, Throwable cause, boolean retryable) {
        super(message, cause);
        this.retryable = retryable;
    }

}
