package vagabounds.dtos.job;

import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;

import java.time.LocalDateTime;
import java.util.List;

// TODO: Colocar as validações necessárias
public record CreateJobRequest(
    String title,
    String description,
    JobType jobType,
    JobModality jobModality,
    List<String> requirements,
    List<String> desiredSkills,
    LocalDateTime expiresAt
) {
}
