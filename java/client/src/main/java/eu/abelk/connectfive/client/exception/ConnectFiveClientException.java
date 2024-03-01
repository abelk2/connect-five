package eu.abelk.connectfive.client.exception;

public class ConnectFiveClientException extends RuntimeException {
    public ConnectFiveClientException(String message) {
        super(message);
    }

    public ConnectFiveClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
