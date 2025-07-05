package vagabounds.dtos.job;

import java.time.LocalDate;

public record JobFilterRequest(
    int page,
    int pageSize,
    String sortBy,
    String direction,
    Boolean isOpen,
    LocalDate createdFrom,
    LocalDate createdTo
) {
}
