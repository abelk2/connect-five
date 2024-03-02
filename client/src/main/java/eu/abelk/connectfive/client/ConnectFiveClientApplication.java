package eu.abelk.connectfive.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.abelk.connectfive.client.flow.DefaultGameFlow;
import eu.abelk.connectfive.client.flow.GameFlow;
import eu.abelk.connectfive.client.service.GameApiService;
import eu.abelk.connectfive.client.service.HttpGameApiService;
import okhttp3.OkHttpClient;

public class ConnectFiveClientApplication {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080";

    public static void main(String... args) {
        GameApiService gameApiService = new HttpGameApiService(new OkHttpClient(), new ObjectMapper(), getBaseUrl(args));
        GameFlow gameFlow = new DefaultGameFlow(gameApiService);
        gameFlow.startGameFlow();
    }

    private static String getBaseUrl(String... args) {
        String result = DEFAULT_BASE_URL;
        if (args.length > 0 && !args[0].trim().isBlank()) {
            result = args[0].trim();
        }
        return result;
    }

}
