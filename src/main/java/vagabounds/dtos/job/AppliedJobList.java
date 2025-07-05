package vagabounds.dtos.job;

import vagabounds.enums.ApplicationStatus;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;

import java.time.LocalDateTime;

public record AppliedJobList(

    String companyName,
    Long jobId,
    String jobTitle,
    JobType jobType,
    JobModality jobModality,
    LocalDateTime appliedAt,
    ApplicationStatus applicationStatus



) {
}
