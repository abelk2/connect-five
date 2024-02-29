package eu.abelk.connectfive.server.domain.disconnect;

import eu.abelk.connectfive.server.domain.exception.ConnectFiveException;

public class DisconnectException extends ConnectFiveException {
    public DisconnectException(String message) {
        super(message);
    }

    public DisconnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
