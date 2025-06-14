package vagabounds.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCompanyRequest(
    @Email String email,
    @Size(min = 8) String password,
    @NotBlank String name,
    @NotBlank String cnpj,
    @NotBlank String address
) {
}
