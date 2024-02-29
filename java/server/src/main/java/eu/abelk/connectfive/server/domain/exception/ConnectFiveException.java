package eu.abelk.connectfive.server.domain.exception;

public class ConnectFiveException extends RuntimeException {

    public ConnectFiveException(String message) {
        super(message);
    }

    public ConnectFiveException(String message, Throwable cause) {
        super(message, cause);
    }

}
