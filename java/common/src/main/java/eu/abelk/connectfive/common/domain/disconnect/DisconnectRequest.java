package eu.abelk.connectfive.common.domain.disconnect;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
public class DisconnectRequest {

    @NotNull(message = "Player identifier must be provided.")
    private final UUID playerId;

}
