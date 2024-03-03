package eu.abelk.connectfive.client.flow;

import eu.abelk.connectfive.client.exception.ConnectFiveClientException;

public class DefaultGameFlowHelper implements GameFlowHelper {
    @Override
    public void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException cause) {
            throw new ConnectFiveClientException("Thread interrupted", cause);
        }
    }

    @Override
    public void terminate() {
        System.exit(1);
    }
}
