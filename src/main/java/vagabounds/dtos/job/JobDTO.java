package vagabounds.dtos.job;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import vagabounds.models.Jobs;
import vagabounds.utils.*;

package vagabounds.dtos.job;

import java.time.LocalDate;
import java.util.Set;
import vagabounds.utils.*;

public record JobDTO(
        Long id,
        Long companyId,
        String title,
        String description,
        JobType jobType,
        Set<String> requirements,
        Set<String> desiredSkills,
        LocalDate deadline
) {

}