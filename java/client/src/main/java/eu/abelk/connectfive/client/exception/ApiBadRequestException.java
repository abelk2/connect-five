package eu.abelk.connectfive.client.exception;

import lombok.Getter;

public class ApiBadRequestException extends ConnectFiveClientException {

    @Getter
    private final boolean retryable;

    public ApiBadRequestException(String message, boolean retryable) {
        super(message);
        this.retryable = retryable;
    }

    public ApiBadRequestException(String message, Throwable cause, boolean retryable) {
        super(message, cause);
        this.retryable = retryable;
    }
}
