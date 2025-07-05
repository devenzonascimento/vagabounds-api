package vagabounds.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCompanyRequest(
    @Email
    @NotBlank
    String email,

    @Size(min = 8)
    @NotBlank
    String password,

    @NotBlank
    String name,

    @NotBlank
    String cnpj,

    @NotBlank
    String address,

    Long groupId

) {
}
