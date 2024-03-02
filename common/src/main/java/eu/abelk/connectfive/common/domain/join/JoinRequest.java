package eu.abelk.connectfive.common.domain.join;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class JoinRequest {

    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Name can only contain alphanumeric characters and underscores.")
    @Size(min = 3, max = 20, message = "The name must be between {min} and {max} characters long.")
    private final String name;

}
