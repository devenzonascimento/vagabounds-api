package vagabounds.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AuthRequest(
    @Email
    String email,

    @NotEmpty
    String password
) {}