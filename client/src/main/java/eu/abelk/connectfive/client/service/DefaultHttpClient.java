package eu.abelk.connectfive.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.abelk.connectfive.client.exception.ApiBadRequestException;
import eu.abelk.connectfive.client.exception.ApiCommunicationException;
import eu.abelk.connectfive.common.domain.error.ErrorResponse;
import okhttp3.*;

import java.io.IOException;

public class DefaultHttpClient implements HttpClient {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DefaultHttpClient(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public <R> void sendRequest(String method, String url, R requestBody) {
        sendRequest(method, url, requestBody == null ? null : serialize(requestBody));
    }

    @Override
    public <T, R> T sendRequest(String method, String url, R requestBody, Class<T> responseType) {
        return deserialize(sendRequest(method, url, requestBody == null ? null : serialize(requestBody)), responseType);
    }

    private String sendRequest(String method, String url, String requestBody) {
        String bodyAsString;
        Request request = new Request.Builder()
            .url(url)
            .method(method, requestBody == null ? null : RequestBody.create(requestBody, MediaType.get("application/json; charset=utf-8")))
            .build();
        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            bodyAsString = body == null ? "" : body.string();
            if (!response.isSuccessful()) {
                ErrorResponse errorResponse = deserialize(bodyAsString, ErrorResponse.class);
                throw new ApiBadRequestException(errorResponse.getMessage(), errorResponse.isRetryable());
            }
        } catch (IOException cause) {
            throw new ApiCommunicationException("Failed to query backend API at " + url, cause);
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
