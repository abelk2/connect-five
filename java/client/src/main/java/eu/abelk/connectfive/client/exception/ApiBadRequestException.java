package eu.abelk.connectfive.client.exception;

public class ApiBadRequestException extends ConnectFiveClientException {
    public ApiBadRequestException(String message) {
        super(message);
    }

    public ApiBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
