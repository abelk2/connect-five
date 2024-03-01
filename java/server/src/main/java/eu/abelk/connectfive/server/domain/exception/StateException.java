package eu.abelk.connectfive.server.domain.exception;

public class StateException extends ConnectFiveServerException {
    public StateException(String message) {
        super(message);
    }

    public StateException(String message, Throwable cause) {
        super(message, cause);
    }
}
