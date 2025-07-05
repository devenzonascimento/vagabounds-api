package vagabounds.dtos.job;

import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;

public record JobFeedDTO(
    Long jobId,
    String title,
    String companyName,
    Boolean isOpen,
    JobType jobType,
    JobModality jobModality,
    double matchPercentage  // de 0.0 a 100.0
) {
}
