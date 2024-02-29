package eu.abelk.connectfive.server.domain.step;

import eu.abelk.connectfive.server.domain.exception.ConnectFiveException;

public class StepException extends ConnectFiveException {
    public StepException(String message) {
        super(message);
    }

    public StepException(String message, Throwable cause) {
        super(message, cause);
    }
}
