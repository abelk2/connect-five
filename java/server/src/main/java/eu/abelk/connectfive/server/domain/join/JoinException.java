package eu.abelk.connectfive.server.domain.join;

import eu.abelk.connectfive.server.domain.exception.ConnectFiveException;

public class JoinException extends ConnectFiveException {
    public JoinException(String message) {
        super(message);
    }

    public JoinException(String message, Throwable cause) {
        super(message, cause);
    }
}
