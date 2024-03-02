package eu.abelk.connectfive.client.exception;

public class ApiCommunicationException extends ConnectFiveClientException {
    public ApiCommunicationException(String message) {
        super(message);
    }

    public ApiCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
