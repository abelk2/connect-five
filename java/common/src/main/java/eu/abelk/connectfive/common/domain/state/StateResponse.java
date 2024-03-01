package eu.abelk.connectfive.common.domain.state;

import eu.abelk.connectfive.common.domain.phase.Phase;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class StateResponse {

    public final Phase phase;
    public final boolean myTurn;
    public final boolean iAmWinner;
    public final String[][] board;
    public final Names names;

}
