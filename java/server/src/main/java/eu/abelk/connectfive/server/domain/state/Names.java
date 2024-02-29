package eu.abelk.connectfive.server.domain.state;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Names {

    @NotNull
    private final String me;
    private final String opponent;

}
