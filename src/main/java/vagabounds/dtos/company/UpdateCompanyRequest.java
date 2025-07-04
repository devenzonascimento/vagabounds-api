package vagabounds.dtos.company;

import jakarta.validation.constraints.Size;

public record UpdateCompanyRequest(
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
        String address,

        @Size(min = 14, max = 18, message = "CNPJ must be between 14 and 18 characters")
        String cnpj
) {
}