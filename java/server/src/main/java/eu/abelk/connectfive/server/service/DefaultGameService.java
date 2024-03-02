package eu.abelk.connectfive.server.service;

import eu.abelk.connectfive.common.domain.disconnect.DisconnectRequest;
import eu.abelk.connectfive.common.domain.state.Names;
import eu.abelk.connectfive.common.domain.state.StateRequest;
import eu.abelk.connectfive.common.domain.state.StateResponse;
import eu.abelk.connectfive.server.dao.GameStateDao;
import eu.abelk.connectfive.server.domain.exception.JoinException;
import eu.abelk.connectfive.common.domain.join.JoinRequest;
import eu.abelk.connectfive.common.domain.join.JoinResponse;
import eu.abelk.connectfive.common.domain.phase.Phase;
import eu.abelk.connectfive.server.domain.exception.StateException;
import eu.abelk.connectfive.server.domain.state.*;
import eu.abelk.connectfive.server.domain.exception.StepException;
import eu.abelk.connectfive.common.domain.step.StepRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class DefaultGameService implements GameService {

    @Autowired
    private GameStateDao gameStateDao;

    @Setter
    private Supplier<UUID> uuidSupplier = UUID::randomUUID;

    @Override
    public JoinResponse join(JoinRequest joinRequest) {
        GameState gameState = gameStateDao.getGameState();
        if (gameState.hasPlayerNamed(joinRequest.getName())) {
            throw new JoinException("There's already a player named '" + joinRequest.getName() + "'.", true);
        }
        if (gameState.getPhase() != Phase.WAITING_FOR_PLAYERS) {
            throw new JoinException("Cannot join game right now: it is already ongoing, or waiting for players to quit.", false);
        }
        UUID playerId = uuidSupplier.get();
        Map<UUID, Player> players = new HashMap<>(gameState.getPlayers());
        players.put(playerId, Player.builder()
            .marker(players.isEmpty() ? Marker.X : Marker.O)
            .name(joinRequest.getName())
            .playersTurn(players.isEmpty())
            .winner(false)
            .build());
        gameStateDao.setGameState(gameState.withPlayers(players)
            .withPhase(players.size() == 2 ? Phase.ONGOING_GAME : Phase.WAITING_FOR_PLAYERS));
        return JoinResponse.builder()
            .playerId(playerId)
            .build();
    }

    @Override
    public StateResponse getState(StateRequest stateRequest) {
        GameState gameState = gameStateDao.getGameState();
        if (!gameState.hasPlayerWithId(stateRequest.getPlayerId())) {
            throw new StateException("Player with ID '" + stateRequest.getPlayerId() + "' does not exist in this game.", false);
        }
        Player player = gameState.getPlayers().get(stateRequest.getPlayerId());
        UUID opponentId = gameState.getOpponentId(stateRequest.getPlayerId());
        return StateResponse.builder()
            .phase(gameState.getPhase())
            .myTurn(player.isPlayersTurn())
            .winner(player.isWinner())
            .board(gameState.boardAsString())
            .names(Names.builder()
                .me(player.getName())
                .opponent(opponentId == null ? null : gameState.getPlayers().get(opponentId).getName())
                .build())
            .build();
    }

    @Override
    public void step(StepRequest stepRequest) {
        GameState gameState = gameStateDao.getGameState();
        if (!gameState.hasPlayerWithId(stepRequest.getPlayerId())) {
            throw new StateException("Player with ID '" + stepRequest.getPlayerId() + "' does not exist in this game.", false);
        }
        if (gameState.getPhase() != Phase.ONGOING_GAME) {
            throw new StepException("Cannot make a step right now: no game is going on.", false);
        }
        Player player = gameState.getPlayers().get(stepRequest.getPlayerId());
        if (!player.isPlayersTurn()) {
            throw new StepException("Cannot make a step: it is not your turn.", true);
        }
        int columnIndex = stepRequest.getColumn() - 1;
        if (gameState.isColumnFull(columnIndex)) {
            throw new StepException("Selected column is full.", true);
        }
        int rowIndex = gameState.findLastEmptyRow(columnIndex);
        Marker[][] board = gameState.getBoard();
        board[rowIndex][columnIndex] = player.getMarker();
        if (gameState.isPlayerWinner(stepRequest.getPlayerId())) {
            Map<UUID, Player> players = new HashMap<>(gameState.getPlayers());
            players.put(stepRequest.getPlayerId(), player.withWinner(true)
                .withPlayersTurn(false));
            gameStateDao.setGameState(gameState.withBoard(board)
                .withPlayers(players)
                .withPhase(Phase.PLAYER_WON));
        } else {
            UUID opponentId = gameState.getOpponentId(stepRequest.getPlayerId());
            Map<UUID, Player> players = new HashMap<>(gameState.getPlayers());
            players.put(stepRequest.getPlayerId(), player.withPlayersTurn(false));
            players.put(opponentId, players.get(opponentId).withPlayersTurn(true));
            gameStateDao.setGameState(gameState.withBoard(board)
                .withPlayers(players));
        }
    }

    @Override
    public void disconnect(DisconnectRequest disconnectRequest) {
        GameState gameState = gameStateDao.getGameState();
        if (!gameState.hasPlayerWithId(disconnectRequest.getPlayerId())) {
            throw new StateException("Player with ID '" + disconnectRequest.getPlayerId() + "' does not exist in this game.", false);
        }
        Map<UUID, Player> players = new HashMap<>(gameState.getPlayers());
        Player disconnectedPlayer = players.get(disconnectRequest.getPlayerId());
        players.remove(disconnectRequest.getPlayerId());
        if (players.isEmpty()) {
            gameStateDao.resetGameState();
        } else if (disconnectedPlayer.isWinner()) {
            // if we set PLAYER_DISCONNECTED phase here, loser will get message that opponent has disconnected
            gameStateDao.setGameState(gameState.withPlayers(players));
        } else {
            gameStateDao.setGameState(gameState.withPhase(Phase.PLAYER_DISCONNECTED)
                .withPlayers(players));
        }
    }

}
