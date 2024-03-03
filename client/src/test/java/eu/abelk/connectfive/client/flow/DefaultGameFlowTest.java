package eu.abelk.connectfive.client.flow;

import eu.abelk.connectfive.client.exception.ApiBadRequestException;
import eu.abelk.connectfive.client.service.GameApiService;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.phase.Phase;
import eu.abelk.connectfive.common.domain.state.Names;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@Listeners(MockitoTestNGListener.class)
public class DefaultGameFlowTest {

    @Mock
    private Scanner in;

    @Mock
    private PrintStream out;

    @Mock
    private GameApiService gameApiService;

    @Mock
    private GameFlowHelper gameFlowHelper;

    @InjectMocks
    private DefaultGameFlow underTest;

    @Test
    public void startGameFlowShouldProduceCorrectGameplayWhenEndsWithDisconnect() {
        // GIVEN
        String playerName = "Kate";
        String opponentName = "Joe";
        UUID playerId = UUID.fromString("5d3557dd-831a-454e-b46f-5d74e3752e8e");
        InOrder inOrder = Mockito.inOrder(in, out, gameApiService, gameFlowHelper);

        StateResponse initialState = StateResponse.builder()
            .phase(Phase.ONGOING_GAME)
            .myTurn(true)
            .winner(false)
            .board(createEmptyBoard())
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        StateResponse secondState = StateResponse.builder()
            .phase(Phase.ONGOING_GAME)
            .myTurn(false)
            .winner(false)
            .board(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "}
            })
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        StateResponse thirdState = StateResponse.builder()
            .phase(Phase.ONGOING_GAME)
            .myTurn(true)
            .winner(false)
            .board(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "}
            })
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        StateResponse fourthState = StateResponse.builder()
            .phase(Phase.PLAYER_DISCONNECTED)
            .myTurn(false)
            .winner(false)
            .board(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", "x", " ", " ", " ", " ", " ", " ", " "}
            })
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();


        given(in.nextLine()).willReturn(playerName, "1", "2");
        given(gameApiService.join(createJoinRequest(playerName))).willReturn(createJoinResponse(playerId));
        given(gameApiService.getState(createStateRequest(playerId))).willReturn(
            initialState, initialState,
            secondState, secondState,
            thirdState, thirdState,
            fourthState, fourthState
        );

        // WHEN
        underTest.startGameFlow();

        // THEN
        then(out).should(inOrder).print("Please enter your name: ");
        thenEmptyBoardPrinted(inOrder);
        then(out).should(inOrder).print("It's your turn Kate, please enter column (1-9): ");
        then(gameApiService).should(inOrder).step(StepRequest.builder()
            .playerId(playerId)
            .column(1)
            .build());
        then(out).should(inOrder).println();
        thenSecondBoardStatePrinted(inOrder);
        then(out).should(inOrder).println("It's Joe's turn, please wait...");
        then(gameFlowHelper).should(inOrder).sleep();
        then(out).should(inOrder).println();
        thenThirdBoardStatePrinted(inOrder);
        then(out).should(inOrder).print("It's your turn Kate, please enter column (1-9): ");
        then(gameApiService).should(inOrder).step(StepRequest.builder()
            .playerId(playerId)
            .column(2)
            .build());
        then(out).should(inOrder).println();
        thenFourthBoardStatePrinted(inOrder);
        then(out).should(inOrder).println("Game has ended because your opponent has left the game.");
    }

    private void thenEmptyBoardPrinted(InOrder inOrder) {
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
    }

    private void thenSecondBoardStatePrinted(InOrder inOrder) {
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
    }

    private void thenThirdBoardStatePrinted(InOrder inOrder) {
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
    }

    private void thenFourthBoardStatePrinted(InOrder inOrder) {
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(2)).print("[x]");
        then(out).should(inOrder, times(7)).print("[ ]");
        then(out).should(inOrder).println();
    }

    @Test
    public void startGameFlowShouldProduceCorrectGameplayWhenEndsWithPlayerWinning() {
        // GIVEN
        String playerName = "Kate";
        String opponentName = "Joe";
        UUID playerId = UUID.fromString("5d3557dd-831a-454e-b46f-5d74e3752e8e");
        InOrder inOrder = Mockito.inOrder(in, out, gameApiService, gameFlowHelper);

        // skipping some steps to keep the test short
        StateResponse almostWinningState = StateResponse.builder()
            .phase(Phase.ONGOING_GAME)
            .myTurn(true)
            .winner(false)
            .board(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", "o", "o", "o", "o", " ", " ", " ", " "}
            })
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        StateResponse winningState = StateResponse.builder()
            .phase(Phase.PLAYER_WON)
            .myTurn(false)
            .winner(true)
            .board(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", " ", " ", " ", " ", " ", " ", " ", " "},
                {"x", "o", "o", "o", "o", " ", " ", " ", " "}
            })
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        given(in.nextLine()).willReturn(playerName, "1");
        given(gameApiService.join(createJoinRequest(playerName))).willReturn(createJoinResponse(playerId));
        given(gameApiService.getState(createStateRequest(playerId))).willReturn(
            almostWinningState, almostWinningState,
            winningState, winningState
        );

        // WHEN
        underTest.startGameFlow();

        // THEN
        then(out).should(inOrder).print("Please enter your name: ");
        thenAlmostWinningBoardPrinted(inOrder);
        then(out).should(inOrder).print("It's your turn Kate, please enter column (1-9): ");
        then(gameApiService).should(inOrder).step(StepRequest.builder()
            .playerId(playerId)
            .column(1)
            .build());
        then(out).should(inOrder).println();
        thenWinningBoardPrinted(inOrder);
        then(out).should(inOrder).println("Game has ended. You have won.");
    }

    private void thenAlmostWinningBoardPrinted(InOrder inOrder) {
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(4)).print("[o]");
        then(out).should(inOrder, times(4)).print("[ ]");
        then(out).should(inOrder).println();
    }

    private void thenWinningBoardPrinted(InOrder inOrder) {
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[x]");
        then(out).should(inOrder, times(4)).print("[o]");
        then(out).should(inOrder, times(4)).print("[ ]");
        then(out).should(inOrder).println();
    }

    @Test
    public void startGameFlowShouldProduceCorrectGameplayWhenEndsWithPlayerLosing() {
        // GIVEN
        String playerName = "Kate";
        String opponentName = "Joe";
        UUID playerId = UUID.fromString("5d3557dd-831a-454e-b46f-5d74e3752e8e");
        InOrder inOrder = Mockito.inOrder(in, out, gameApiService, gameFlowHelper);

        // skipping some steps to keep the test short
        StateResponse almostLosingState = StateResponse.builder()
            .phase(Phase.ONGOING_GAME)
            .myTurn(false)
            .winner(false)
            .board(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", "x", "x", "o", "x", "x", " ", " ", " "}
            })
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        StateResponse losingState = StateResponse.builder()
            .phase(Phase.PLAYER_WON)
            .myTurn(false)
            .winner(false)
            .board(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", " ", " ", " ", " ", " ", " ", " ", " "},
                {"o", "x", "x", "o", "x", "x", " ", " ", " "}
            })
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        given(in.nextLine()).willReturn(playerName);
        given(gameApiService.join(createJoinRequest(playerName))).willReturn(createJoinResponse(playerId));
        given(gameApiService.getState(createStateRequest(playerId))).willReturn(
            almostLosingState, almostLosingState,
            losingState, losingState
        );

        // WHEN
        underTest.startGameFlow();

        // THEN
        then(out).should(inOrder).print("Please enter your name: ");
        thenAlmostLosingBoardPrinted(inOrder);
        then(out).should(inOrder).println("It's Joe's turn, please wait...");
        then(out).should(inOrder).println();
        thenLosingBoardPrinted(inOrder);
        then(out).should(inOrder).println("Game has ended. You have lost.");
    }

    private void thenAlmostLosingBoardPrinted(InOrder inOrder) {
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(2)).print("[x]");
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(2)).print("[x]");
        then(out).should(inOrder, times(3)).print("[ ]");
        then(out).should(inOrder).println();
    }

    private void thenLosingBoardPrinted(InOrder inOrder) {
        then(out).should(inOrder, times(9)).print("[ ]");
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(8)).print("[ ]");
        then(out).should(inOrder).println();
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(2)).print("[x]");
        then(out).should(inOrder).print("[o]");
        then(out).should(inOrder, times(2)).print("[x]");
        then(out).should(inOrder, times(3)).print("[ ]");
        then(out).should(inOrder).println();
    }

    @Test
    public void startGameFlowShouldHandleIncorrectStepInput() {
        // GIVEN
        String playerName = "Kate";
        String opponentName = "Joe";
        UUID playerId = UUID.fromString("5d3557dd-831a-454e-b46f-5d74e3752e8e");
        InOrder inOrder = Mockito.inOrder(in, out, gameApiService, gameFlowHelper);

        StateResponse initialState = StateResponse.builder()
            .phase(Phase.ONGOING_GAME)
            .myTurn(true)
            .winner(false)
            .board(createEmptyBoard())
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        // this will force the game loop to exit at the end of the test
        StateResponse exitState = StateResponse.builder()
            .phase(Phase.PLAYER_DISCONNECTED)
            .myTurn(true)
            .winner(false)
            .board(createEmptyBoard())
            .names(Names.builder()
                .me(playerName)
                .opponent(opponentName)
                .build())
            .build();

        StepRequest incorrectStepRequest = StepRequest.builder()
            .column(42)
            .playerId(playerId)
            .build();

        ApiBadRequestException retryableException = new ApiBadRequestException("Column number must be between 1 and 9.", true);
        ApiBadRequestException nonRetryableException = new ApiBadRequestException("Fatal error.", false);

        given(in.nextLine()).willReturn(playerName, "notANumber", "42", "42", "1");
        given(gameApiService.join(createJoinRequest(playerName))).willReturn(createJoinResponse(playerId));
        given(gameApiService.getState(createStateRequest(playerId))).willReturn(initialState, initialState, exitState);
        willThrow(retryableException, nonRetryableException).given(gameApiService).step(incorrectStepRequest);

        // WHEN
        underTest.startGameFlow();

        // THEN
        then(out).should(inOrder).print("Please enter your name: ");
        thenEmptyBoardPrinted(inOrder);
        then(out).should(inOrder).print("It's your turn Kate, please enter column (1-9): ");
        then(out).should(inOrder).print("Input is not a number. Please try again: ");
        then(gameApiService).should(inOrder).step(incorrectStepRequest);
        then(out).should(inOrder).print("Column number must be between 1 and 9. Please try again: ");
        then(gameApiService).should(inOrder).step(incorrectStepRequest);
        then(out).should(inOrder).print("Fatal error. Terminating.");
        then(gameFlowHelper).should(inOrder).terminate();
    }

    private String[][] createEmptyBoard() {
        return new String[][]{
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "}
        };
    }

    private StateRequest createStateRequest(UUID playerId) {
        return StateRequest.builder().playerId(playerId).build();
    }

    private JoinResponse createJoinResponse(UUID playerId) {
        return JoinResponse.builder().playerId(playerId).build();
    }

    private JoinRequest createJoinRequest(String playerName) {
        return JoinRequest.builder().name(playerName).build();
    }
}