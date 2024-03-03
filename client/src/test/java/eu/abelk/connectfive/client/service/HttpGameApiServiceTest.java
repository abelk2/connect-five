package eu.abelk.connectfive.client.service;

import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class HttpGameApiServiceTest {

    @Mock
    private HttpClient httpClient;

    private HttpGameApiService underTest;

    @BeforeMethod
    public void setUp() {
        underTest = new HttpGameApiService(httpClient, "http://example.com");
    }

    @Test
    public void joinShouldDelegateToHttpClient() {
        // GIVEN
        JoinRequest joinRequest = JoinRequest.builder().build();
        JoinResponse expected = JoinResponse.builder().build();

        given(httpClient.sendRequest("POST", "http://example.com/join", joinRequest, JoinResponse.class)).willReturn(expected);

        // WHEN
        JoinResponse actual = underTest.join(joinRequest);

        // THEN
        assertEquals(actual, expected);
    }

    @Test
    public void getStateShouldDelegateToHttpClient() {
        // GIVEN
        UUID playerId = UUID.fromString("5d3557dd-831a-454e-b46f-5d74e3752e8e");
        StateRequest stateRequest = StateRequest.builder()
            .playerId(playerId)
            .build();
        StateResponse expected = StateResponse.builder().build();

        given(httpClient.sendRequest("GET", "http://example.com/state?playerId=5d3557dd-831a-454e-b46f-5d74e3752e8e", null, StateResponse.class)).willReturn(expected);

        // WHEN
        StateResponse actual = underTest.getState(stateRequest);

        // THEN
        assertEquals(actual, expected);
    }

    @Test
    public void stepShouldDelegateToHttpClient() {
        // GIVEN
        StepRequest stepRequest = StepRequest.builder().build();

        // WHEN
        underTest.step(stepRequest);

        // THEN
        then(httpClient).should().sendRequest("POST", "http://example.com/step", stepRequest);
    }

    @Test
    public void disconnectShouldDelegateToHttpClient() {
        // GIVEN
        DisconnectRequest disconnectRequest = DisconnectRequest.builder().build();

        // WHEN
        underTest.disconnect(disconnectRequest);

        // THEN
        then(httpClient).should().sendRequest("POST", "http://example.com/disconnect", disconnectRequest);
    }
}