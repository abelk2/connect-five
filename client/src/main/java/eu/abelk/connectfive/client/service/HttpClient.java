package eu.abelk.connectfive.client.service;

public interface HttpClient {
    <R> void sendRequest(String method, String path, R requestBody);

    <T, R> T sendRequest(String method, String path, R requestBody, Class<T> responseType);
}
