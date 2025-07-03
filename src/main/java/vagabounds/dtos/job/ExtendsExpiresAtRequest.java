package vagabounds.dtos.job;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ExtendsExpiresAtRequest(
    @NotNull
    Long jobId,

    @NotNull
    LocalDateTime newExpiresAt
) {
}
