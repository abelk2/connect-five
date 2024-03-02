package eu.abelk.connectfive.common.domain.state;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
public class StateRequest {

    @NotNull(message = "Player identifier must be provided.")
    private final UUID playerId;

}
