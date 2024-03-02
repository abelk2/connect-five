package eu.abelk.connectfive.server.domain.state;

import eu.abelk.connectfive.common.domain.phase.Phase;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@With
public class GameState {

    private final Phase phase;
    private final Map<UUID, Player> players;
    private final Marker[][] board;

    public boolean hasPlayerNamed(String name) {
        return players.values()
            .stream()
            .anyMatch(player -> Objects.equals(player.getName(), name));
    }

    public boolean hasPlayerWithId(UUID playerId) {
        return players.keySet()
            .stream()
            .anyMatch(id -> Objects.equals(playerId, id));
    }

    public boolean isColumnFull(int columnIndex) {
        boolean result = true;
        for (Marker[] row : board) {
            if (row[columnIndex] == Marker.EMPTY) {
                result = false;
                break;
            }
        }
        return result;
    }

    public int findLastEmptyRow(int columnIndex) {
        int result = -1;
        for (int rowIndex = board.length - 1; rowIndex >= 0; rowIndex--) {
            if (board[rowIndex][columnIndex] == Marker.EMPTY) {
                result = rowIndex;
                break;
            }
        }
        return result;
    }

    public String[][] boardAsString() {
        return Arrays.stream(board)
            .map(row -> Arrays.stream(row)
                .map(Marker::getText)
                .toArray(String[]::new))
            .toArray(String[][]::new);
    }

    public UUID getOpponentId(UUID playerId) {
        return players.keySet()
            .stream()
            .filter(id -> !Objects.equals(id, playerId))
            .findAny()
            .orElse(null);
    }

    public boolean isPlayerWinner(UUID playerId) {
        Player player = players.get(playerId);
        return isMarkerWinner(player.getMarker());
    }

    private boolean isMarkerWinner(Marker marker) {
        return hasFiveInRows(marker) || hasFiveInColumns(marker) || hasFiveInDiagonals(marker);
    }

    private boolean hasFiveInRows(Marker marker) {
        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            int consecutiveCount = 0;
            for (int columnIndex = 0; columnIndex < board[rowIndex].length; columnIndex++) {
                if (board[rowIndex][columnIndex] == marker) {
                    consecutiveCount++;
                    if (consecutiveCount == 5) {
                        return true;
                    }
                } else {
                    consecutiveCount = 0;
                }
            }
        }
        return false;
    }

    private boolean hasFiveInColumns(Marker marker) {
        for (int columnIndex = 0; columnIndex < board[0].length; columnIndex++) {
            int consecutiveCount = 0;
            for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
                if (board[rowIndex][columnIndex] == marker) {
                    consecutiveCount++;
                    if (consecutiveCount == 5) {
                        return true;
                    }
                } else {
                    consecutiveCount = 0;
                }
            }
        }
        return false;
    }

    private boolean hasFiveInDiagonals(Marker marker) {
        for (int rowIndex = 0; rowIndex <= board.length - 5; rowIndex++) {
            for (int columnIndex = 0; columnIndex <= board[rowIndex].length - 5; columnIndex++) {
                if (checkDiagonal(rowIndex, columnIndex, marker) || checkDiagonalReverse(rowIndex, columnIndex, marker)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonal(int startRow, int startCol, Marker marker) {
        int consecutiveCount = 0;
        for (int i = 0; i < 5; i++) {
            if (board[startRow + i][startCol + i] == marker) {
                consecutiveCount++;
                if (consecutiveCount == 5) {
                    return true;
                }
            } else {
                consecutiveCount = 0;
            }
        }
        return false;
    }

    private boolean checkDiagonalReverse(int startRow, int startColumn, Marker marker) {
        int consecutiveCount = 0;
        for (int i = 0; i < 5; i++) {
            if (board[startRow + 4 - i][startColumn + i] == marker) {
                consecutiveCount++;
                if (consecutiveCount == 5) {
                    return true;
                }
            } else {
                consecutiveCount = 0;
            }
        }
        return false;
    }

}
