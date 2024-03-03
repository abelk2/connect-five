package eu.abelk.connectfive.client.service;

import eu.abelk.connectfive.common.constants.EndpointConstants;
import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;

public class HttpGameApiService implements GameApiService {
    private final HttpClient httpClient;
    private final String baseUrl;

    public HttpGameApiService(HttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public JoinResponse join(JoinRequest joinRequest) {
        return httpClient.sendRequest("POST", baseUrl + EndpointConstants.JOIN_PATH, joinRequest, JoinResponse.class);
    }

    @Override
    public StateResponse getState(StateRequest stateRequest) {
        return httpClient.sendRequest("GET", baseUrl + EndpointConstants.STATE_PATH + "?playerId=" + stateRequest.getPlayerId(),null, StateResponse.class);
    }

    @Override
    public void step(StepRequest stepRequest) {
        httpClient.sendRequest("POST", baseUrl + EndpointConstants.STEP_PATH, stepRequest);
    }

    @Override
    public void disconnect(DisconnectRequest disconnectRequest) {
        httpClient.sendRequest("POST", baseUrl + EndpointConstants.DISCONNECT_PATH, disconnectRequest);
    }
}
