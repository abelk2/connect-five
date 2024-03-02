package eu.abelk.connectfive.client.flow;

import eu.abelk.connectfive.client.exception.ApiBadRequestException;
import eu.abelk.connectfive.client.exception.ConnectFiveClientException;
import eu.abelk.connectfive.client.service.GameApiService;
import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.phase.Phase;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Predicate;

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
        addShutdownHook(playerId);
        waitForAllPlayers(playerId);
        boolean gameEnded = false;
        while (!gameEnded) {
            StateResponse state = getState(playerId);
            printBoard(state.getBoard());
            if (state.getPhase() == Phase.ONGOING_GAME) {
                if (state.isMyTurn()) {
                    this.out.print("It's your turn " + state.getNames().getMe() + ", please enter column (1-9): ");
                    doStep(playerId);
                } else {
                    this.out.println("It's " + state.getNames().getOpponent() + "'s turn, please wait...");
                    waitForState(playerId, desiredState -> desiredState.isMyTurn() || desiredState.getPhase() != Phase.ONGOING_GAME);
                }
                this.out.println();
            } else if (state.getPhase() == Phase.PLAYER_WON) {
                this.out.println("Game has ended. You have " + (state.winner ? "won" : "lost") + ".");
                gameEnded = true;
            } else if (state.getPhase() == Phase.PLAYER_DISCONNECTED) {
                this.out.println("Game has ended because your opponent has left the game.");
                gameEnded = true;
            }
        }
    }

    private void addShutdownHook(UUID playerId) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gameApiService.disconnect(DisconnectRequest.builder()
                .playerId(playerId)
                .build());
        }));
    }

    private void waitForAllPlayers(UUID playerId) {
        this.out.println("Waiting for other player to join...\n");
        waitForState(playerId, state -> state.getPhase() != Phase.WAITING_FOR_PLAYERS);
    }

    private void waitForState(UUID playerId, Predicate<StateResponse> predicate) {
        boolean waiting = true;
        while (waiting) {
            StateResponse state = getState(playerId);
            if (!predicate.test(state)) {
                sleep();
            } else {
                waiting = false;
            }
        }
    }

    private void printBoard(String[][] board) {
        for (String[] row : board) {
            for (String marker : row) {
                this.out.print("[" + marker + "]");
            }
            this.out.println();
        }
    }

    private void doStep(UUID playerId) {
        boolean correctNumber = false;
        while (!correctNumber) {
            String numberString = in.nextLine();
            try {
                int number = Integer.parseInt(numberString);
                gameApiService.step(StepRequest.builder()
                    .playerId(playerId)
                    .column(number)
                    .build());
                correctNumber = true;
            } catch (ApiBadRequestException exception) {
                handleBadRequest(exception);
            } catch (NumberFormatException exception) {
                this.out.print("Input is not a number. Please try again: ");
            }
        }
    }

    private StateResponse getState(UUID playerId) {
        return gameApiService.getState(StateRequest.builder()
            .playerId(playerId)
            .build());
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException cause) {
            throw new ConnectFiveClientException("Thread interrupted", cause);
        }
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
                handleBadRequest(exception);
            }
        }
    }

    private void handleBadRequest(ApiBadRequestException exception) {
        if (exception.isRetryable()) {
            this.out.print(exception.getMessage() + " Please try again: ");
        } else {
            this.out.print(exception.getMessage() + " Terminating.");
            System.exit(1);
        }
    }

}
