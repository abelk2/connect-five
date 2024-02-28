package eu.abelk.connectfive.server.domain.join;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class JoinRequest {

    @NotBlank(message = "Name must be provided.")
    @Size(min = 3, max = 20, message = "Name must be at least {min}, at most {max} characters long.")
    private final String name;

}
