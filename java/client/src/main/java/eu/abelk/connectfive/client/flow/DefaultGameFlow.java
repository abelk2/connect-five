package eu.abelk.connectfive.client.flow;

import eu.abelk.connectfive.client.exception.ApiBadRequestException;
import eu.abelk.connectfive.client.service.GameApiService;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.UUID;

public class DefaultGameFlow implements GameFlow {

    private final Scanner in;
    private final PrintStream out;
    private final GameApiService gameApiService;

    public DefaultGameFlow(GameApiService gameApiService) {
        this.gameApiService = gameApiService;
        this.in = new Scanner(System.in);
        this.out = System.out;
    }

    public DefaultGameFlow(Scanner scanner, PrintStream printStream, GameApiService gameApiService) {
        this.in = scanner;
        this.out = printStream;
        this.gameApiService = gameApiService;
    }

    @Override
    public void startGameFlow() {
        UUID playerId = promptPlayer();
        StateResponse state = gameApiService.getState(StateRequest.builder()
            .playerId(playerId)
            .build());
        this.out.println(state);
    }

    private UUID promptPlayer() {
        this.out.print("Please enter your name: ");
        while (true) {
            String name = in.nextLine();
            try {
                JoinResponse joinResponse = gameApiService.join(JoinRequest.builder()
                    .name(name)
                    .build());
                return joinResponse.getPlayerId();
            } catch (ApiBadRequestException exception) {
                this.out.print(exception.getMessage() + " Please try again: ");
            }
        }
    }

}
