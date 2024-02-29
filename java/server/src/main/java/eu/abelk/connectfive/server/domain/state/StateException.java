package eu.abelk.connectfive.server.domain.state;

import eu.abelk.connectfive.server.domain.exception.ConnectFiveException;

public class StateException extends ConnectFiveException {
    public StateException(String message) {
        super(message);
    }

    public StateException(String message, Throwable cause) {
        super(message, cause);
    }
}
