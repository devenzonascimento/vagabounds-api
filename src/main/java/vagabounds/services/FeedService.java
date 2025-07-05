package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vagabounds.dtos.job.JobFeedDTO;
import vagabounds.models.Candidate;
import vagabounds.models.Job;
import vagabounds.repositories.CandidateRepository;
import vagabounds.repositories.JobRepository;
import vagabounds.security.SecurityUtils;
import vagabounds.specifications.JobSpecifications;

import java.util.List;
import java.util.Set;

@Service
public class FeedService {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    JobRepository jobRepository;

    public Page<JobFeedDTO> getFeed(
        List<String> skills,
        Pageable pageable
    ) {
        Candidate candidate = getCurrentCandidate();

        Page<Job> openJobs = jobRepository.findAll(
            JobSpecifications.openAndEligible(candidate),
            pageable
        );

        return openJobs.map(job -> {
            double match = 0.0;

            if (!skills.isEmpty()) {
                match = calculateMatch(skills, job);
            }

            return new JobFeedDTO(
                job.getId(),
                job.getTitle(),
                job.getCompany().getName(),
                job.getIsOpen(),
                job.getJobType(),
                job.getJobModality(),
                match
            );
        });
    }

    private double calculateMatch(List<String> skills, Job j) {
        Set<String> requirements = j.getRequirements();
        if (requirements.isEmpty()) return 100.0;

        long intersection = skills.stream().filter(requirements::contains).count();
        return (intersection * 100.0) / requirements.size();
    }

    private Candidate getCurrentCandidate() {
        var accountId = SecurityUtils.getAccountId();

        var candidate = candidateRepository.findByAccountId(accountId).orElse(null);

        if (candidate == null || candidate.getIsDeleted()) {
            throw new RuntimeException("Candidate not found.");
        }

        return candidate;
    }
}

