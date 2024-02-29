package eu.abelk.connectfive.server.dao;

import eu.abelk.connectfive.server.domain.state.GameState;

public interface GameStateDao {

    GameState getGameState();

    void setGameState(GameState gameState);

    void resetGameState();

}
