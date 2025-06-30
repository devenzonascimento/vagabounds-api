package vagabounds.dtos.generic;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResult<T>(
   int page,
   int pageSize,
   int totalPages,
   List<T> items
) {
    public static <T> PageResult<T> fromRawPage(Page<T> p) {
        return new PageResult<T>(
            p.getNumber(),
            p.getSize(),
            p.getTotalPages(),
            p.getContent()
        );
    }
}
