package vagabounds.dtos.job;

import jakarta.validation.constraints.NotNull;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;

import java.util.List;

public record UpdateJobRequest(
    @NotNull
    Long jobId,
    String title,
    String description,
    JobType jobType,
    JobModality jobModality,
    List<String> requirements,
    List<String> desiredSkills
) {
}
