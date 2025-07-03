package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vagabounds.dtos.application.ApplyToJobRequest;
import vagabounds.dtos.application.ApproveCandidateRequest;
import vagabounds.dtos.application.RejectCandidateRequest;
import vagabounds.enums.ApplicationStatus;
import vagabounds.models.Application;
import vagabounds.models.Candidate;
import vagabounds.repositories.ApplicationRepository;
import vagabounds.repositories.CandidateRepository;
import vagabounds.repositories.JobRepository;
import vagabounds.security.SecurityUtils;

import java.util.HashSet;

@Service
public class ApplicationService {
    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    EmailService emailService;

    public void applyToJob(ApplyToJobRequest request) {
        var candidate = getCurrentCandidate();

        var job = jobRepository.findById(request.jobId()).orElse(null);

        if (job == null || job.getIsDeleted()) {
            throw new RuntimeException("Job not found.");
        }

        var applicationFound = applicationRepository.findByJobIdAndCandidateId(job.getId(), candidate.getId()).orElse(null);

        if (applicationFound != null) {
            throw new RuntimeException("You already applied to this job.");
        }

        var jobApplication = new Application(
            job,
            candidate,
            request.candidatePresentation(),
            new HashSet<>(request.candidateSkills())
        );

        jobApplication = applicationRepository.save(jobApplication);

        if (jobApplication.getStatus().equals(ApplicationStatus.APPLIED)) {
            emailService.sendAppliedEmail();
        } else {
            emailService.sendAutoRejectedEmail();
        }
    }

    public void approveCandidate(ApproveCandidateRequest request) {
        var jobApplication = findById(request.applicationId());

        jobApplication.approve(request.decisionReason());

        applicationRepository.save(jobApplication);

        emailService.sendApprovedEmail();
    }

    public void rejectCandidate(RejectCandidateRequest request) {
        var jobApplication = findById(request.applicationId());

        jobApplication.reject(request.decisionReason());

        applicationRepository.save(jobApplication);

        emailService.sendRejectedEmail();
    }

    public Application findById(Long applicationId) {
        var jobApplication = applicationRepository.findById(applicationId).orElse(null);

        if (jobApplication == null) {
            throw new RuntimeException("Job application not found.");
        }

        return jobApplication;
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
