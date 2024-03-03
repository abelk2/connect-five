package eu.abelk.connectfive.server.web;

import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.error.ErrorResponse;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;
import eu.abelk.connectfive.server.domain.exception.ConnectFiveServerException;
import eu.abelk.connectfive.server.service.GameService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class GameRestControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameRestController underTest;

    @Test
    public void joinShouldDelegateToGameService() {
        // GIVEN
        JoinRequest joinRequest = JoinRequest.builder().build();
        JoinResponse expected = JoinResponse.builder().build();

        given(gameService.join(joinRequest)).willReturn(expected);

        // WHEN
        JoinResponse actual = underTest.join(joinRequest);

        // THEN
        assertEquals(actual, expected);
        then(gameService).should().join(joinRequest);
    }

    @Test
    public void getStateShouldDelegateToGameService() {
        // GIVEN
        StateRequest stateRequest = StateRequest.builder().build();
        StateResponse expected = StateResponse.builder().build();

        given(gameService.getState(stateRequest)).willReturn(expected);

        // WHEN
        StateResponse actual = underTest.getState(stateRequest);

        // THEN
        assertEquals(actual, expected);
        then(gameService).should().getState(stateRequest);
    }

    @Test
    public void stepShouldDelegateToGameService() {
        // GIVEN
        StepRequest stepRequest = StepRequest.builder().build();

        // WHEN
        underTest.step(stepRequest);

        // THEN
        then(gameService).should().step(stepRequest);
    }

    @Test
    public void disconnectShouldDelegateToGameService() {
        // GIVEN
        DisconnectRequest disconnectRequest = DisconnectRequest.builder().build();

        // WHEN
        underTest.disconnect(disconnectRequest);

        // THEN
        then(gameService).should().disconnect(disconnectRequest);
    }

    @Test
    public void handleConnectFiveServerExceptionShouldCreateResponseEntityWithErrorResponse() {
        // GIVEN
        ConnectFiveServerException exception = new ConnectFiveServerException("ouch", true);
        ErrorResponse expectedResponse = ErrorResponse.builder()
            .type("ConnectFiveServerException")
            .message("ouch")
            .retryable(true)
            .build();

        // WHEN
        ResponseEntity<ErrorResponse> result = underTest.handleConnectFiveServerException(exception);

        // THEN
        assertEquals(result.getBody(), expectedResponse);
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void handleBindExceptionShouldCreateResponseEntityWithErrorResponseAndDefaultMessageWhenNoFieldErrors() {
        // GIVEN
        BindException exception = new BindException(new Object(), "objectName");
        ErrorResponse expectedResponse = ErrorResponse.builder()
            .type("BindException")
            .message("Bad request")
            .retryable(true)
            .build();

        // WHEN
        ResponseEntity<ErrorResponse> result = underTest.handleBindException(exception);

        // THEN
        assertEquals(result.getBody(), expectedResponse);
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void handleBindExceptionShouldCreateResponseEntityWithErrorResponse() {
        // GIVEN
        FieldError fieldError = new FieldError("objectName", "field", "bindErrorMessage");
        BindException exception = new BindException(new Object(), "objectName");
        exception.addError(fieldError);

        ErrorResponse expectedResponse = ErrorResponse.builder()
            .type("BindException")
            .message("bindErrorMessage")
            .retryable(true)
            .build();

        // WHEN
        ResponseEntity<ErrorResponse> result = underTest.handleBindException(exception);

        // THEN
        assertEquals(result.getBody(), expectedResponse);
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}