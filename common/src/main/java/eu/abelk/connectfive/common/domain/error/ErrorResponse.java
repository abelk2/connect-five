package eu.abelk.connectfive.common.domain.error;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ErrorResponse {

    private final String type;
    private final String message;
    private final boolean retryable;

}
