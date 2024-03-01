package eu.abelk.connectfive.server.domain.exception;

public class DisconnectException extends ConnectFiveServerException {
    public DisconnectException(String message) {
        super(message);
    }

    public DisconnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
