package eu.abelk.connectfive.server.web;

import eu.abelk.connectfive.server.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.server.domain.join.JoinRequest;
import eu.abelk.connectfive.server.domain.join.JoinResponse;
import eu.abelk.connectfive.server.domain.state.StateRequest;
import eu.abelk.connectfive.server.domain.state.StateResponse;
import eu.abelk.connectfive.server.domain.step.StepRequest;
import eu.abelk.connectfive.server.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameRestController {

    @Autowired
    private GameService gameService;

    @PostMapping(path = "/join")
    public JoinResponse join(@Valid @RequestBody JoinRequest joinRequest) {
        return gameService.join(joinRequest);
    }

    @GetMapping(path = "/state")
    public StateResponse getState(@Valid StateRequest stateRequest) {
        return gameService.getState(stateRequest);
    }

    @PostMapping(path = "/step")
    public void step(@Valid @RequestBody StepRequest stepRequest) {
        gameService.step(stepRequest);
    }

    @PostMapping(path = "/disconnect")
    public void disconnect(@Valid @RequestBody DisconnectRequest disconnectRequest) {
        gameService.disconnect(disconnectRequest);
    }

}
