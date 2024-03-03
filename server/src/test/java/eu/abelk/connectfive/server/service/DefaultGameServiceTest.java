package eu.abelk.connectfive.server.service;

import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.phase.Phase;
import eu.abelk.connectfive.common.domain.state.Names;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.common.domain.step.StepRequest;
import eu.abelk.connectfive.server.dao.GameStateDao;
import eu.abelk.connectfive.server.domain.exception.DisconnectException;
import eu.abelk.connectfive.server.domain.exception.JoinException;
import eu.abelk.connectfive.server.domain.exception.StateException;
import eu.abelk.connectfive.server.domain.exception.StepException;
import eu.abelk.connectfive.server.domain.state.GameState;
import eu.abelk.connectfive.server.domain.state.Marker;
import eu.abelk.connectfive.server.domain.state.Player;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class DefaultGameServiceTest {

    private static final UUID TEST_UUID_1 = UUID.fromString("de43505e-57f7-479c-8d8c-ca5d70e18a64");
    private static final UUID TEST_UUID_2 = UUID.fromString("5d3557dd-831a-454e-b46f-5d74e3752e8e");

    @Mock
    private GameStateDao gameStateDao;

    @InjectMocks
    private DefaultGameService underTest;

    @Test(expectedExceptions = JoinException.class, expectedExceptionsMessageRegExp = "There's already a player named 'Kate'\\.")
    public void joinShouldThrowJoinExceptionWhenGameStateAlreadyHasGivenPlayerName() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Map.of(TEST_UUID_1, Player.builder()
                .name("Kate")
                .build()))
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.join(JoinRequest.builder()
            .name("Kate")
            .build());

        // THEN JoinException is thrown
    }

    @Test(expectedExceptions = JoinException.class, expectedExceptionsMessageRegExp = "Cannot join game right now: it is already ongoing, or waiting for players to quit\\.")
    public void joinShouldThrowJoinExceptionWhenGamePhaseIsNotWaitingForPlayers() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.PLAYER_DISCONNECTED)
            .players(Map.of(TEST_UUID_1, Player.builder()
                .name("Kate")
                .build()))
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.join(JoinRequest.builder()
            .name("George")
            .build());

        // THEN JoinException is thrown
    }

    @Test
    public void joinShouldUpdateGameStateWithNewPlayerAndKeepPhaseAsWaitingForPlayersWhenFirstPlayerJoins() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Collections.emptyMap())
            .board(createEmptyBoard())
            .build();

        underTest.setUuidSupplier(() -> TEST_UUID_1);
        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        JoinResponse response = underTest.join(JoinRequest.builder()
            .name("Kate")
            .build());

        // THEN
        assertEquals(response.getPlayerId(), TEST_UUID_1);
        then(gameStateDao).should().setGameState(
            GameState.builder()
                .phase(Phase.WAITING_FOR_PLAYERS)
                .players(Map.of(TEST_UUID_1, Player.builder()
                    .marker(Marker.X)
                    .name("Kate")
                    .playersTurn(true)
                    .winner(false)
                    .build()))
                .board(createEmptyBoard())
                .build());
    }

    @Test
    public void joinShouldUpdateGameStateWithNewPlayerAndSetPhaseToOngoingGameWhenSecondPlayerJoins() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Map.of(TEST_UUID_1, Player.builder()
                .marker(Marker.X)
                .name("Kate")
                .playersTurn(true)
                .winner(false)
                .build()))
            .board(createEmptyBoard())
            .build();

        underTest.setUuidSupplier(() -> TEST_UUID_2);
        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        JoinResponse response = underTest.join(JoinRequest.builder()
            .name("Joe")
            .build());

        // THEN
        assertEquals(response.getPlayerId(), TEST_UUID_2);
        then(gameStateDao).should().setGameState(
            GameState.builder()
                .phase(Phase.ONGOING_GAME)
                .players(Map.of(
                    TEST_UUID_1, Player.builder()
                        .marker(Marker.X)
                        .name("Kate")
                        .playersTurn(true)
                        .winner(false)
                        .build(),
                    TEST_UUID_2, Player.builder()
                        .marker(Marker.O)
                        .name("Joe")
                        .playersTurn(false)
                        .winner(false)
                        .build()))
                .board(createEmptyBoard())
                .build());
    }

    @Test(expectedExceptions = StateException.class, expectedExceptionsMessageRegExp = "Player with ID 'de43505e-57f7-479c-8d8c-ca5d70e18a64' does not exist in this game\\.")
    public void getStateShouldThrowStateExceptionWhenGameStateHasNoGivenPlayerId() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Collections.emptyMap())
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.getState(StateRequest.builder()
            .playerId(TEST_UUID_1)
            .build());

        // THEN StateException is thrown
    }

    @Test
    public void getStateShouldReturnStateWithoutOpponentWhenPlayerHasNoOpponentYet() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Map.of(TEST_UUID_1, Player.builder()
                .name("Joe")
                .marker(Marker.X)
                .winner(false)
                .playersTurn(true)
                .build()))
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        StateResponse expected = StateResponse.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .myTurn(true)
            .winner(false)
            .board(createEmptyStringBoard())
            .names(Names.builder()
                .me("Joe")
                .opponent(null)
                .build())
            .build();

        // WHEN
        StateResponse actual = underTest.getState(StateRequest.builder()
            .playerId(TEST_UUID_1)
            .build());

        // THEN
        assertEquals(actual, expected);
    }

    @Test
    public void getStateShouldReturnStateWithOpponentWhenPlayerHasOpponent() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.ONGOING_GAME)
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Joe")
                    .marker(Marker.X)
                    .winner(false)
                    .playersTurn(true)
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(false)
                    .build()))
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        StateResponse expected = StateResponse.builder()
            .phase(Phase.ONGOING_GAME)
            .myTurn(true)
            .winner(false)
            .board(createEmptyStringBoard())
            .names(Names.builder()
                .me("Joe")
                .opponent("Kate")
                .build())
            .build();

        // WHEN
        StateResponse actual = underTest.getState(StateRequest.builder()
            .playerId(TEST_UUID_1)
            .build());

        // THEN
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = StepException.class, expectedExceptionsMessageRegExp = "Player with ID 'de43505e-57f7-479c-8d8c-ca5d70e18a64' does not exist in this game\\.")
    public void stepShouldThrowStepExceptionWhenGameStateHasNoGivenPlayerId() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Collections.emptyMap())
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.step(StepRequest.builder()
            .column(1)
            .playerId(TEST_UUID_1)
            .build());

        // THEN StepException is thrown
    }

    @Test(expectedExceptions = StepException.class, expectedExceptionsMessageRegExp = "Cannot make a step right now: no game is going on\\.")
    public void stepShouldThrowStepExceptionWhenPhaseIsNotOngoingGame() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Map.of(TEST_UUID_1, Player.builder()
                .name("Joe")
                .marker(Marker.X)
                .winner(false)
                .playersTurn(true)
                .build()))
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.step(StepRequest.builder()
            .column(1)
            .playerId(TEST_UUID_1)
            .build());

        // THEN StepException is thrown
    }

    @Test(expectedExceptions = StepException.class, expectedExceptionsMessageRegExp = "Cannot make a step: it is not your turn\\.")
    public void stepShouldThrowStepExceptionWhenItIsNotThePlayersTurn() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.ONGOING_GAME)
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Joe")
                    .marker(Marker.X)
                    .winner(false)
                    .playersTurn(true)
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(false)
                    .build()))
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.step(StepRequest.builder()
            .column(1)
            .playerId(TEST_UUID_2)
            .build());

        // THEN StepException is thrown
    }

    @Test(expectedExceptions = StepException.class, expectedExceptionsMessageRegExp = "Selected column is full\\.")
    public void stepShouldThrowStepExceptionWhenSelectedColumnIsFull() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.ONGOING_GAME)
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Joe")
                    .marker(Marker.X)
                    .winner(false)
                    .playersTurn(true)
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(false)
                    .build()))
            .board(new Marker[][]{
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.step(StepRequest.builder()
            .column(1)
            .playerId(TEST_UUID_1)
            .build());

        // THEN StepException is thrown
    }

    @Test
    public void stepShouldSetPlayerWinnerAndSetPlayerWonPhaseWhenPlayerHasWon() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.ONGOING_GAME)
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Joe")
                    .marker(Marker.X)
                    .winner(false)
                    .playersTurn(true)
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(false)
                    .build()))
            .board(new Marker[][]{
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.O, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.step(StepRequest.builder()
            .column(1)
            .playerId(TEST_UUID_1)
            .build());

        // THEN
        then(gameStateDao).should().setGameState(
            GameState.builder()
                .phase(Phase.PLAYER_WON)
                .players(Map.of(
                    TEST_UUID_1, Player.builder()
                        .name("Joe")
                        .marker(Marker.X)
                        .winner(true)
                        .playersTurn(false)
                        .build(),
                    TEST_UUID_2, Player.builder()
                        .name("Kate")
                        .marker(Marker.O)
                        .winner(false)
                        .playersTurn(false)
                        .build()))
                .board(new Marker[][]{
                    { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.O, Marker.O, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
                })
                .build());
    }

    @Test
    public void stepShouldSwapPlayersTurnWhenNoPlayerHasWon() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.ONGOING_GAME)
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Joe")
                    .marker(Marker.X)
                    .winner(false)
                    .playersTurn(true)
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(false)
                    .build()))
            .board(new Marker[][]{
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.step(StepRequest.builder()
            .column(1)
            .playerId(TEST_UUID_1)
            .build());

        // THEN
        then(gameStateDao).should().setGameState(
            GameState.builder()
                .phase(Phase.ONGOING_GAME)
                .players(Map.of(
                    TEST_UUID_1, Player.builder()
                        .name("Joe")
                        .marker(Marker.X)
                        .winner(false)
                        .playersTurn(false)
                        .build(),
                    TEST_UUID_2, Player.builder()
                        .name("Kate")
                        .marker(Marker.O)
                        .winner(false)
                        .playersTurn(true)
                        .build()))
                .board(new Marker[][]{
                    { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.O, Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
                })
                .build());
    }

    @Test(expectedExceptions = DisconnectException.class, expectedExceptionsMessageRegExp = "Player with ID 'de43505e-57f7-479c-8d8c-ca5d70e18a64' does not exist in this game\\.")
    public void disconnectShouldThrowDisconnectExceptionWhenGameStateHasNoGivenPlayerId() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Collections.emptyMap())
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.disconnect(DisconnectRequest.builder()
            .playerId(TEST_UUID_1)
            .build());

        // THEN DisconnectException is thrown
    }

    @Test
    public void disconnectShouldResetGameStateWhenNoPlayerIsLeftAfterDisconnect() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .players(Map.of(TEST_UUID_1, Player.builder()
                .name("Joe")
                .marker(Marker.X)
                .winner(false)
                .playersTurn(false)
                .build()))
            .board(createEmptyBoard())
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.disconnect(DisconnectRequest.builder()
            .playerId(TEST_UUID_1)
            .build());

        // THEN
        then(gameStateDao).should().resetGameState();
    }

    @Test
    public void disconnectShouldRemovePlayerFromPlayersListWhenPlayerIsWinner() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.PLAYER_WON)
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Joe")
                    .marker(Marker.X)
                    .winner(true)
                    .playersTurn(false)
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(false)
                    .build()))
            .board(new Marker[][]{
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.O, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.disconnect(DisconnectRequest.builder()
            .playerId(TEST_UUID_1)
            .build());

        // THEN
        then(gameStateDao).should().setGameState(
            GameState.builder()
                .phase(Phase.PLAYER_WON)
                .players(Map.of(TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(false)
                    .build()))
                .board(new Marker[][]{
                    { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.O, Marker.O, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
                })
                .build());
    }

    @Test
    public void disconnectShouldRemovePlayerFromPlayersListAndSetPlayerDisconnectedPhase() {
        // GIVEN
        GameState gameState = GameState.builder()
            .phase(Phase.ONGOING_GAME)
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Joe")
                    .marker(Marker.X)
                    .winner(false)
                    .playersTurn(false)
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(true)
                    .build()))
            .board(new Marker[][]{
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.O, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        given(gameStateDao.getGameState()).willReturn(gameState);

        // WHEN
        underTest.disconnect(DisconnectRequest.builder()
            .playerId(TEST_UUID_1)
            .build());

        // THEN
        then(gameStateDao).should().setGameState(
            GameState.builder()
                .phase(Phase.PLAYER_DISCONNECTED)
                .players(Map.of(TEST_UUID_2, Player.builder()
                    .name("Kate")
                    .marker(Marker.O)
                    .winner(false)
                    .playersTurn(true)
                    .build()))
                .board(new Marker[][]{
                    { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                    { Marker.X, Marker.O, Marker.O, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
                })
                .build());
    }

    private Marker[][] createEmptyBoard() {
        return new Marker[][]{
            { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
            { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
            { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
            { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
            { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
            { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
        };
    }

    private String[][] createEmptyStringBoard() {
        return new String[][]{
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "}
        };
    }
}