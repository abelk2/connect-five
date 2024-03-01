package eu.abelk.connectfive.server.domain.exception;

public class JoinException extends ConnectFiveServerException {
    public JoinException(String message) {
        super(message);
    }

    public JoinException(String message, Throwable cause) {
        super(message, cause);
    }
}
