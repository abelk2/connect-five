package eu.abelk.connectfive.common.domain.join;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
public class JoinResponse {

    private final UUID playerId;

}
