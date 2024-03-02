package eu.abelk.connectfive.server.domain.state;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class Player {

    private final Marker marker;
    private final String name;
    private final boolean playersTurn;
    private final boolean winner;

}
