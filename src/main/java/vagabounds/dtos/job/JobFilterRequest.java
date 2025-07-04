package vagabounds.dtos.job;

import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

public record JobFilterRequest(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int pageSize,
    @RequestParam(defaultValue = "createdAt") String sortBy,
    @RequestParam(defaultValue = "DESC") String direction,
    @RequestParam Boolean isOpen,
    @RequestParam LocalDate createdFrom,
    @RequestParam LocalDate createdTo
) {
}
