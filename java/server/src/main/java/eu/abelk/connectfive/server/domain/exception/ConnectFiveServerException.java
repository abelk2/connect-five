package eu.abelk.connectfive.server.domain.exception;

public class ConnectFiveServerException extends RuntimeException {

    public ConnectFiveServerException(String message) {
        super(message);
    }

    public ConnectFiveServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
