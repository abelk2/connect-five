package eu.abelk.connectfive.server.web;

import eu.abelk.connectfive.server.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.server.domain.join.JoinRequest;
import eu.abelk.connectfive.server.domain.join.JoinResponse;
import eu.abelk.connectfive.server.domain.phase.Phase;
import eu.abelk.connectfive.server.domain.state.Names;
import eu.abelk.connectfive.server.domain.state.StateRequest;
import eu.abelk.connectfive.server.domain.state.StateResponse;
import eu.abelk.connectfive.server.domain.step.StepRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GameRestController {

    @PostMapping(path = "/join")
    public JoinResponse join(@Valid @RequestBody JoinRequest joinRequest) {
        return JoinResponse.builder()
            .playerId(UUID.randomUUID())
            .build();
    }

    @GetMapping(path = "/state")
    public StateResponse getState(@Valid StateRequest stateRequest) {
        return StateResponse.builder()
            .phase(Phase.ONGOING_GAME)
            .myTurn(true)
            .iAmWinner(false)
            .board(new String[][] {
                { "", "", "", "", "", "", "", "", "" },
                { "", "", "", "", "", "", "", "", "" },
                { "", "", "", "", "", "", "", "", "" },
                { "", "", "", "o", "", "", "", "", "" },
                { "", "", "", "x", "", "", "", "", "" },
                { "", "", "", "x", "", "", "", "", "" },
                { "", "", "o", "x", "o", "o", "", "", "" }
            })
            .names(Names.builder()
                .me("John")
                .opponent("Mary")
                .build())
            .build();
    }

    @PostMapping(path = "/step")
    public void step(@Valid @RequestBody StepRequest stepRequest) {

    }

    @PostMapping(path = "/disconnect")
    public void disconnect(@Valid @RequestBody DisconnectRequest disconnectRequest) {

    }

}
