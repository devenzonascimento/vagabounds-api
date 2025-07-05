package vagabounds.dtos.job;

import vagabounds.dtos.candidate.CandidateDTO;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;
import vagabounds.models.Job;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record JobDetailsReponse(
    Long id,
    String title,
    String company,
    String description,
    JobType jobType,
    Set<String> requirements,
    Set<String> desiredSkills,
    JobModality jobModality,
    String companyAddress,
    int candidatesCount,
    List<CandidateDTO> candidates,
    Boolean isOpen,
    LocalDateTime createdAt,
    LocalDateTime closedAt,
    LocalDateTime expiresAt
) {
    public static JobDetailsReponse fromJob(Job job) {
        if (job == null) {
            return null;
        }

        return new JobDetailsReponse(
            job.getId(),
            job.getTitle(),
            job.getCompany().getName(),
            job.getDescription(),
            job.getJobType(),
            job.getRequirements(),
            job.getDesiredSkills(),
            job.getJobModality(),
            job.getCompany().getAddress(),
            job.getApplications().size(),
            job.getApplications()
                .stream()
                .map(a -> CandidateDTO.fromCandidate(a.getCandidate()))
                .toList(),
            job.getIsOpen(),
            job.getCreatedAt(),
            job.getClosedAt(),
            job.getExpiresAt()
        );
    }

    public static List<JobDetailsReponse> fromJobs(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return new ArrayList<>();
        }

        return jobs.stream().map(JobDetailsReponse::fromJob).toList();
    }
}
