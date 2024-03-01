package eu.abelk.connectfive.server.dao;

import eu.abelk.connectfive.server.domain.phase.Phase;
import eu.abelk.connectfive.server.domain.state.GameState;
import eu.abelk.connectfive.server.domain.state.Marker;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class InMemoryGameStateDao implements GameStateDao {

    private GameState gameState;

    public InMemoryGameStateDao() {
        resetGameState();
    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void resetGameState() {
        this.gameState = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .board(createEmptyBoard())
            .players(Collections.emptyMap())
            .build();
    }

    private Marker[][] createEmptyBoard() {
        Marker[][] result = new Marker[6][9];
        for (int rowIndex = 0; rowIndex < 6; ++rowIndex) {
            for (int columnIndex = 0; columnIndex < 9; ++columnIndex) {
                result[rowIndex][columnIndex] = Marker.EMPTY;
            }
        }
        return result;
    }
}
