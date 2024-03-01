package eu.abelk.connectfive.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.abelk.connectfive.client.exception.ApiBadRequestException;
import eu.abelk.connectfive.client.exception.ApiCommunicationException;
import eu.abelk.connectfive.common.constants.EndpointConstants;
import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.error.ErrorResponse;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;
import okhttp3.*;

import java.io.IOException;

public class HttpGameApiService implements GameApiService {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public HttpGameApiService(OkHttpClient httpClient, ObjectMapper objectMapper, String baseUrl) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
    }

    @Override
    public JoinResponse join(JoinRequest joinRequest) {
        return sendRequest("POST", EndpointConstants.JOIN_PATH, joinRequest, JoinResponse.class);
    }

    @Override
    public StateResponse getState(StateRequest stateRequest) {
        return sendRequest("GET", EndpointConstants.STATE_PATH + "?playerId=" + stateRequest.getPlayerId(),null, StateResponse.class);
    }

    @Override
    public void step(StepRequest stepRequest) {
        sendRequest("POST", EndpointConstants.STEP_PATH, stepRequest);
    }

    @Override
    public void disconnect(DisconnectRequest disconnectRequest) {
        sendRequest("POST", EndpointConstants.DISCONNECT_PATH, disconnectRequest);
    }

    private <R> void sendRequest(String method, String path, R requestBody) {
        sendRequest(method, path, requestBody == null ? null : serialize(requestBody));
    }

    private <T, R> T sendRequest(String method, String path, R requestBody, Class<T> responseType) {
        return deserialize(sendRequest(method, path, requestBody == null ? null : serialize(requestBody)), responseType);
    }

    private String sendRequest(String method, String path, String requestBody) {
        String bodyAsString;
        Request request = new Request.Builder()
            .url(baseUrl + path)
            .method(method, requestBody == null ? null : RequestBody.create(requestBody, MediaType.get("application/json; charset=utf-8")))
            .build();
        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            bodyAsString = body == null ? "" : body.string();
            if (!response.isSuccessful()) {
                ErrorResponse errorResponse = deserialize(bodyAsString, ErrorResponse.class);
                throw new ApiBadRequestException(errorResponse.getMessage());
            }
        } catch (IOException cause) {
            throw new ApiCommunicationException("Failed to query backend API at " + baseUrl + path, cause);
        }
        return bodyAsString;
    }

    private <R> String serialize(R object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException cause) {
            throw new ApiCommunicationException("Failed to serialize request: " + object, cause);
        }
    }

    private <T> T deserialize(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException cause) {
            throw new ApiCommunicationException("Failed to deserialize response as " + type + ":" + json, cause);
        }
    }
}
