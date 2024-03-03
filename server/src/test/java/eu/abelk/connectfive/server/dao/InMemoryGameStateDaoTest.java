package eu.abelk.connectfive.server.dao;

import eu.abelk.connectfive.common.domain.phase.Phase;
import eu.abelk.connectfive.server.domain.state.GameState;
import eu.abelk.connectfive.server.domain.state.Marker;
import org.mockito.InjectMocks;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertEquals;

@Listeners(MockitoTestNGListener.class)
public class InMemoryGameStateDaoTest {

    @InjectMocks
    private InMemoryGameStateDao underTest;

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void setGameStateShouldThrowIllegalArgumentExceptionWhenNullGiven() {
        // GIVEN
        // WHEN
        underTest.setGameState(null);

        // THEN IllegalArgumentException is thrown
    }

    @Test
    public void setGameStateShouldSetState() {
        // GIVEN
        GameState gameState = GameState.builder().build();

        // WHEN
        underTest.setGameState(gameState);

        // THEN
        assertEquals(underTest.getGameState(), gameState);
    }

    @Test
    public void resetGameStateShouldSetInitialPhaseWithNoPlayersAndEmptyBoard() {
        // GIVEN
        GameState expected = GameState.builder()
            .phase(Phase.WAITING_FOR_PLAYERS)
            .board(new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .players(Collections.emptyMap())
            .build();

        // WHEN
        underTest.resetGameState();

        // THEN
        assertEquals(underTest.getGameState(), expected);
    }
}