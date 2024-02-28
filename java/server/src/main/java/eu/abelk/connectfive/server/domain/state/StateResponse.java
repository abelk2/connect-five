package eu.abelk.connectfive.server.domain.state;

import eu.abelk.connectfive.server.domain.phase.Phase;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class StateResponse {

    @NotNull
    public final Phase phase;

    @NotNull
    public final boolean myTurn;

    @NotNull
    public final boolean iAmWinner;

    @NotNull
    public final String[][] board;

    @NotNull
    public final Names names;

}
