package eu.abelk.connectfive.server.domain.step;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
public class StepRequest {

    @NotNull(message = "Player identifier must be provided.")
    private final UUID playerId;

    @NotNull(message = "Column number must be provided.")
    @Min(value = 1, message = "Column number must be at least {value}.")
    @Max(value = 9, message = "Column number must be larger than {value}.")
    private final int column;

}
