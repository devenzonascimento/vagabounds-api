package vagabounds.dtos.job;

import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;
import vagabounds.models.Job;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record JobDTO(
        Long id,
        Long companyId,
        String companyName,
        String title,
        String description,
        JobType jobType,
        Set<String> requirements,
        Set<String> desiredSkills,
        JobModality jobModality,
        String companyAddress,
        Boolean isOpen,
        LocalDateTime createdAt,
        LocalDateTime closedAt,
        LocalDateTime expiresAt
) {
    public static JobDTO fromJob(Job job) {
        if (job == null) {
            return null;
        }

        return new JobDTO(
            job.getId(),
            job.getCompany().getId(),
            job.getCompany().getName(),
            job.getTitle(),
            job.getDescription(),
            job.getJobType(),
            job.getRequirements(),
            job.getDesiredSkills(),
            job.getJobModality(),
            job.getCompany().getAddress(),
            job.getIsOpen(),
            job.getCreatedAt(),
            job.getClosedAt(),
            job.getExpiresAt()
        );
    }

    public static List<JobDTO> fromJobs(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return new ArrayList<>();
        }

        return jobs.stream().map(JobDTO::fromJob).toList();
    }
}