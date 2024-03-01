package eu.abelk.connectfive.server.domain.exception;

public class StepException extends ConnectFiveServerException {
    public StepException(String message) {
        super(message);
    }

    public StepException(String message, Throwable cause) {
        super(message, cause);
    }
}
