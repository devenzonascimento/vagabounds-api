package vagabounds.dtos.application;

import vagabounds.enums.ApplicationStatus;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;

import java.time.LocalDate;

public record AppliedJobFilter(
    Long candidateId,
    ApplicationStatus status,
    LocalDate appliedAt,
    JobType jobType,
    JobModality jobModality
) {}
