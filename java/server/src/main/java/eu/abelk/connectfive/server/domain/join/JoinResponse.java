package eu.abelk.connectfive.server.domain.join;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
public class JoinResponse {

    @NotNull
    private final UUID playerId;

}
