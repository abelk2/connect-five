package eu.abelk.connectfive.server.domain.state;

import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.*;

@Listeners(MockitoTestNGListener.class)
public class GameStateTest {

    private static final UUID TEST_UUID_1 = UUID.fromString("de43505e-57f7-479c-8d8c-ca5d70e18a64");
    private static final UUID TEST_UUID_2 = UUID.fromString("5d3557dd-831a-454e-b46f-5d74e3752e8e");

    @Test(dataProvider = "emptyNames", expectedExceptions = IllegalArgumentException.class)
    public void hasPlayerNamedShouldThrowIllegalArgumentExceptionWhenEmptyNameGiven(String name) {
        // GIVEN
        GameState underTest = GameState.builder().build();

        // WHEN
        underTest.hasPlayerNamed(name);

        // THEN IllegalArgumentException is thrown
    }

    @Test
    public void hasPlayerNamedShouldReturnFalseWhenGameStateHasNoSuchPlayer() {
        // GIVEN
        GameState underTest = GameState.builder()
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Kate")
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Joe")
                    .build()
            ))
            .build();

        // WHEN
        boolean result = underTest.hasPlayerNamed("Harry");

        // THEN
        assertFalse(result);
    }

    @Test
    public void hasPlayerNamedShouldReturnTrueWhenGameStateHasPlayerWithGivenName() {
        // GIVEN
        GameState underTest = GameState.builder()
            .players(Map.of(
                TEST_UUID_1, Player.builder()
                    .name("Kate")
                    .build(),
                TEST_UUID_2, Player.builder()
                    .name("Joe")
                    .build()
            ))
            .build();

        // WHEN
        boolean result = underTest.hasPlayerNamed("Kate");

        // THEN
        assertTrue(result);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void hasPlayerWithIdShouldThrowIllegalArgumentExceptionWhenNullGiven() {
        // GIVEN
        GameState underTest = GameState.builder().build();

        // WHEN
        underTest.hasPlayerWithId(null);

        // THEN IllegalArgumentException is thrown
    }

    @Test
    public void hasPlayerWithIdShouldReturnFalseWhenGameStateHasNoSuchPlayerId() {
        // GIVEN
        GameState underTest = GameState.builder()
            .players(Map.of(TEST_UUID_1, Player.builder().build()))
            .build();

        // WHEN
        boolean result = underTest.hasPlayerWithId(TEST_UUID_2);

        // THEN
        assertFalse(result);
    }

    @Test
    public void hasPlayerWithIdShouldReturnTrueWhenGameStateHasGivenPlayerId() {
        // GIVEN
        GameState underTest = GameState.builder()
            .players(Map.of(TEST_UUID_1, Player.builder().build()))
            .build();

        // WHEN
        boolean result = underTest.hasPlayerWithId(TEST_UUID_1);

        // THEN
        assertTrue(result);
    }

    @Test(dataProvider = "outOfRangeColumnIndices", expectedExceptions = IllegalArgumentException.class)
    public void isColumnFullShouldThrowIllegalArgumentExceptionWhenColumnIndexIsOutOfRange(int columnIndex) {
        // GIVEN
        GameState underTest = GameState.builder().build();

        // WHEN
        underTest.isColumnFull(columnIndex);

        // THEN IllegalArgumentException is thrown
    }

    @Test
    public void isColumnFullShouldReturnTrueWhenChosenColumnIsFull() {
        // GIVEN
        GameState underTest = GameState.builder()
            .board(new Marker[][] {
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        // WHEN
        boolean result = underTest.isColumnFull(0);

        // THEN
        assertTrue(result);
    }

    @Test
    public void isColumnFullShouldReturnFalseWhenChosenColumnIsNotFull() {
        // GIVEN
        GameState underTest = GameState.builder()
            .board(new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        // WHEN
        boolean result = underTest.isColumnFull(0);

        // THEN
        assertFalse(result);
    }

    @Test(dataProvider = "outOfRangeColumnIndices", expectedExceptions = IllegalArgumentException.class)
    public void findLastEmptyRowShouldThrowIllegalArgumentExceptionWhenColumnIndexIsOutOfRange(int columnIndex) {
        // GIVEN
        GameState underTest = GameState.builder().build();

        // WHEN
        underTest.findLastEmptyRow(columnIndex);

        // THEN IllegalArgumentException is thrown
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findLastEmptyRowShouldThrowIllegalArgumentExceptionWhenColumnIsFull() {
        // GIVEN
        GameState underTest = GameState.builder()
            .board(new Marker[][] {
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        // WHEN
        underTest.findLastEmptyRow(0);

        // THEN IllegalArgumentException is thrown
    }

    @Test
    public void findLastEmptyRowShouldReturnLastEmptyRowWhenColumnIsNotFull() {
        // GIVEN
        GameState underTest = GameState.builder()
            .board(new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();
        int expected = 1;

        // WHEN
        int actual = underTest.findLastEmptyRow(0);

        // THEN
        assertEquals(actual, expected);
    }

    @Test
    public void boardAsStringShouldReturnBoardAs2dStringArray() {
        // GIVEN
        GameState underTest = GameState.builder()
            .board(new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        String[][] expected = {
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " ", " "},
            {"x", " ", " ", " ", " ", " ", " ", " ", " "},
            {"o", "x", " ", " ", " ", " ", " ", " ", " "},
            {"o", "x", " ", " ", " ", " ", " ", " ", " "},
            {"x", "o", " ", " ", " ", " ", " ", " ", " "}
        };

        // WHEN
        String[][] actual = underTest.boardAsString();

        // THEN
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getOpponentIdShouldThrowIllegalArgumentExceptionWhenPlayerIdIsNull() {
        // GIVEN
        GameState underTest = GameState.builder().build();

        // WHEN
        underTest.getOpponentId(null);

        // THEN IllegalArgumentException is thrown
    }

    @Test
    public void getOpponentIdShouldReturnNullWhenPlayerHasNoOpponent() {
        // GIVEN
        GameState underTest = GameState.builder()
            .players(Map.of(TEST_UUID_1, Player.builder().build()))
            .build();

        // WHEN
        UUID result = underTest.getOpponentId(TEST_UUID_1);

        // THEN
        assertNull(result);
    }

    @Test
    public void getOpponentIdShouldReturnOpponentIdWhenPlayerHasOpponent() {
        // GIVEN
        GameState underTest = GameState.builder()
            .players(Map.of(
                TEST_UUID_1, Player.builder().build(),
                TEST_UUID_2, Player.builder().build()))
            .build();

        // WHEN
        UUID result = underTest.getOpponentId(TEST_UUID_1);

        // THEN
        assertEquals(result, TEST_UUID_2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void isPlayerWinnerShouldThrowIllegalArgumentExceptionWhenPlayerIdIsNull() {
        // GIVEN
        GameState gameState = GameState.builder().build();

        // WHEN
        gameState.isPlayerWinner(null);

        // THEN IllegalArgumentException is thrown
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void isPlayerWinnerShouldThrowIllegalArgumentExceptionWhenNoSuchPlayerExists() {
        // GIVEN
        GameState gameState = GameState.builder()
            .players(Collections.emptyMap())
            .build();

        // WHEN
        gameState.isPlayerWinner(TEST_UUID_1);

        // THEN IllegalArgumentException is thrown
    }

    @Test
    public void isPlayerWinnerShouldReturnFalseWhenPlayerHasNotWon() {
        // GIVEN
        GameState underTest = GameState.builder()
            .players(Map.of(TEST_UUID_1, Player.builder()
                .marker(Marker.X)
                .build()))
            .board(new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            })
            .build();

        // WHEN
        boolean result = underTest.isPlayerWinner(TEST_UUID_1);

        // THEN
        assertFalse(result);
    }

    @Test(dataProvider = "winnerBoardsForX")
    public void isPlayerWinnerShouldReturnTrueWhenPlayerHasWon(Marker[][] board) {
        // GIVEN
        GameState underTest = GameState.builder()
            .players(Map.of(TEST_UUID_1, Player.builder()
                .marker(Marker.X)
                .build()))
            .board(board)
            .build();

        // WHEN
        boolean result = underTest.isPlayerWinner(TEST_UUID_1);

        // THEN
        assertTrue(result);
    }

    @DataProvider
    public Object[] emptyNames() {
        return new String[] {
            null,
            "",
            "   "
        };
    }

    @DataProvider
    public Object[] outOfRangeColumnIndices() {
        return new Integer[] {
            -1, 9
        };
    }

    @DataProvider
    public Object[] winnerBoardsForX() {
        return new Marker[][][] {
            // horizontal
            new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.X, Marker.X, Marker.X, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.O, Marker.O, Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.X, Marker.O, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.O, Marker.X, Marker.O, Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            },
            // vertical
            new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            },
            // top to bottom diagonal
            new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.O, Marker.X, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.O, Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.X, Marker.X, Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            },
            // bottom to top diagonal
            new Marker[][] {
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.EMPTY, Marker.X, Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.EMPTY, Marker.X, Marker.O, Marker.O, Marker.X, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY },
                { Marker.X, Marker.O, Marker.X, Marker.X, Marker.O, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY, Marker.EMPTY }
            },
        };
    }
}