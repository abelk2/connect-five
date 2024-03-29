package eu.abelk.connectfive.server.web;

import eu.abelk.connectfive.common.constants.EndpointConstants;
import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.error.ErrorResponse;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;
import eu.abelk.connectfive.server.domain.exception.ConnectFiveServerException;
import eu.abelk.connectfive.server.service.GameService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class GameRestController {

    @Autowired
    private GameService gameService;

    @PostMapping(path = EndpointConstants.JOIN_PATH)
    public JoinResponse join(@Valid @RequestBody JoinRequest joinRequest) {
        return gameService.join(joinRequest);
    }

    @GetMapping(path = EndpointConstants.STATE_PATH)
    public StateResponse getState(@Valid StateRequest stateRequest) {
        return gameService.getState(stateRequest);
    }

    @PostMapping(path = EndpointConstants.STEP_PATH)
    public void step(@Valid @RequestBody StepRequest stepRequest) {
        gameService.step(stepRequest);
    }

    @PostMapping(path = EndpointConstants.DISCONNECT_PATH)
    public void disconnect(@Valid @RequestBody DisconnectRequest disconnectRequest) {
        gameService.disconnect(disconnectRequest);
    }

    @ExceptionHandler(ConnectFiveServerException.class)
    public ResponseEntity<ErrorResponse> handleConnectFiveServerException(ConnectFiveServerException exception) {
        log.error("Caught exception.", exception);
        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(exception.getClass().getSimpleName())
            .message(exception.getMessage())
            .retryable(exception.isRetryable())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {
        log.error("Caught bind exception.", exception);
        FieldError fieldError = exception.getFieldError();
        ErrorResponse errorResponse = ErrorResponse.builder()
            .type(exception.getClass().getSimpleName())
            .message(fieldError == null ? "Bad request" : fieldError.getDefaultMessage())
            .retryable(true)
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

}
