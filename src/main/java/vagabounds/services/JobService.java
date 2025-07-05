package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vagabounds.dtos.application.AppliedJobFilter;
import vagabounds.dtos.candidate.UpdateCandidateRequest;
import vagabounds.dtos.job.*;
import vagabounds.enums.GroupPermission;
import vagabounds.models.Application;
import vagabounds.models.Candidate;
import vagabounds.models.Company;
import vagabounds.models.Job;
import vagabounds.repositories.ApplicationRepository;
import vagabounds.repositories.CandidateRepository;
import vagabounds.repositories.CompanyRepository;
import vagabounds.repositories.JobRepository;
import vagabounds.security.SecurityUtils;
import vagabounds.specifications.ApplicationSpecifications;
import vagabounds.specifications.JobSpecifications;
import java.util.List;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    CompanyRepository companyRepository;
    
    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    ResumeService resumeService;

    public void createJob(CreateJobRequest request) {
        var company = getCurrentCompany();

        var job = new Job(
            company,
            request.title(),
            request.description(),
            request.jobType(),
            request.jobModality(),
            request.requirements(),
            request.desiredSkills(),
            request.expiresAt()
        );

        jobRepository.save(job);
    }

    public void updateJob(UpdateJobRequest request) {
        var job = this.findById(request.jobId());

        var company = getCurrentCompany();

        if (!job.getCompany().getId().equals(company.getId()) || !GroupPermission.OWNER.equals(company.getPermission())) {
            throw new RuntimeException("You don't have permission to edit job informations, this job is not belongs to your company");
        }

        if (!job.getIsOpen() && job.getClosedAt() != null) {
            throw new RuntimeException("This job is closed and no longer allows information updates.");
        }

        validateCanManageJob(company, job);

        job.update(
            request.title(),
            request.description(),
            request.jobType(),
            request.jobModality(),
            request.requirements(),
            request.desiredSkills()
        );

        jobRepository.save(job);
    }

    public Job findById(Long jobId) {
        var job = jobRepository.findById(jobId).orElse(null);
        var company = getCurrentCompany();

        validateCanViewJob(company, job);

        return job;
    }

    public Page<Job> listJobsWithFilter(JobFilterRequest filter) {
        var company = getCurrentCompany();

        validateCanViewGroupJobs(company);

        Specification<Job> spec = JobSpecifications.belongsToCompany(company.getId())
            .and(JobSpecifications.isOpen(filter.isOpen()))
            .and(JobSpecifications.createdBetween(filter.createdFrom(), filter.createdTo()));

        Sort sort = Sort.by(Sort.Direction.fromString(filter.direction()), filter.sortBy());
        Pageable pageable = PageRequest.of(filter.page(), filter.pageSize(), sort);

        return jobRepository.findAll(spec, pageable);
    }

    public List<Job> findAllJobsByCompany() {
        var company = getCurrentCompany();

        var jobs = jobRepository.findAllByCompanyId(company.getId());

        if (jobs.isEmpty()) {
            return jobs;
        }

        validateCanViewGroupJobs(company);

        return jobs.stream().filter(j -> !j.getIsDeleted()).toList();
    }

    public void extendExpiresAt(ExtendsExpiresAtRequest request) {
        var job = findById(request.jobId());

        var company = getCurrentCompany();

        validateCanManageJob(company, job);

        job.extendExpiresAt(request.newExpiresAt());

        jobRepository.save(job);
    }

    public void closeManually(Long jobId) {
        var job = findById(jobId);

        var company = getCurrentCompany();

        validateCanManageJob(company, job);

        job.closeManually();

        jobRepository.save(job);
    }

    private Company getCurrentCompany() {
        var accountId = SecurityUtils.getAccountId();

        var company = companyRepository.findByAccountId(accountId).orElse(null);

        if (company == null || company.getIsDeleted()) {
            throw new RuntimeException("Company not found.");
        }

        return company;
    }

    private Candidate getCurrentCandidate() {
        var accountId = SecurityUtils.getAccountId();

        var candidate = candidateRepository.findByAccountId(accountId).orElse(null);

        if (candidate == null || candidate.getIsDeleted()) {
            throw new RuntimeException("Candidate not found.");
        }

        return candidate;
    }

    public List<AppliedJobList> findAllAppliedJobs(AppliedJobFilter filter) {
        var candidate = getCurrentCandidate();

        Specification<Application> spec = Specification.anyOf(
                ApplicationSpecifications.hasCandidateId(candidate.getId())
            ).and(ApplicationSpecifications.hasStatus(filter.status()))
            .and(ApplicationSpecifications.hasAppliedAt(filter.appliedAt()))
            .and(ApplicationSpecifications.hasJobType(filter.jobType()))
            .and(ApplicationSpecifications.hasJobModality(filter.jobModality()));

        return applicationRepository.findAll(spec).stream()
            .map(app -> new AppliedJobList(
                app.getJob().getCompany().getName(),
                app.getJob().getId(),
                app.getJob().getTitle(),
                app.getJob().getJobType(),
                app.getJob().getJobModality(),
                app.getAppliedAt(),
                app.getStatus()
            ))
            .toList();
    }

    public UpdateCandidateRequest candidateInformationAndResume(Long jobId, Long candidateId) {

        var app = applicationRepository.findByJobIdAndCandidateId(jobId, candidateId)
            .orElseThrow(() -> new RuntimeException("There is no application job with the given information."));

        var candidate = app.getCandidate();

        var resumeUrl = resumeService.loadCandidateResume(candidateId);

        return new UpdateCandidateRequest(
            candidate.getId(),
            candidate.getName(),
            candidate.getAddress(),
            candidate.getEducation(),
            candidate.getCourse(),
            candidate.getSemester(),
            candidate.getGraduationYear(),
            resumeUrl
        );
    }


    public List<JobDTO> findGroupJobs() {
        var company = getCurrentCompany();

        validateCanViewGroupJobs(company);

        if (company.getGroup() == null) {
            throw new RuntimeException("Company is not part of any group.");
        }

        List<Job> jobs;

        if (GroupPermission.MEMBER.equals(company.getPermission())) {
            jobs = jobRepository.findAllByGroupIdAndCompanyIdAndIsActiveTrue(
                    company.getGroup().getId(),
                    company.getId()
            );
        } else if (GroupPermission.ADMIN.equals(company.getPermission()) ||
                GroupPermission.OWNER.equals(company.getPermission())) {
            jobs = jobRepository.findAllByGroupIdAndIsActiveTrue(company.getGroup().getId());
        } else {
            throw new RuntimeException("Invalid permission level.");
        }

        return JobDTO.fromJobs(jobs);
    }


    private void validateCanManageJob(Company company, Job job) {
        if (GroupPermission.OWNER.equals(company.getPermission())) {
            if (company.getGroup() != null && job.getCompany().getGroup() != null &&
                    company.getGroup().getId().equals(job.getCompany().getGroup().getId())) {
                return;
            }
            if (company.getId().equals(job.getCompany().getId())) {
                return;
            }
        }

        if (GroupPermission.ADMIN.equals(company.getPermission()) ||
                GroupPermission.MEMBER.equals(company.getPermission())) {
            if (company.getId().equals(job.getCompany().getId())) {
                return;
            }
        }

        throw new RuntimeException("You don't have permission to manage this job.");
    }

    private void validateCanViewJob(Company company, Job job) {
        if (GroupPermission.OWNER.equals(company.getPermission())) {
            if (company.getGroup() != null && job.getCompany().getGroup() != null &&
                    company.getGroup().getId().equals(job.getCompany().getGroup().getId())) {
                return;
            }
            if (company.getId().equals(job.getCompany().getId())) {
                return;
            }
        }

        if (GroupPermission.ADMIN.equals(company.getPermission())) {
            if (company.getGroup() != null && job.getCompany().getGroup() != null &&
                    company.getGroup().getId().equals(job.getCompany().getGroup().getId())) {
                return;
            }
        }

        if (GroupPermission.MEMBER.equals(company.getPermission())) {
            if (company.getId().equals(job.getCompany().getId())) {
                return;
            }
        }

        throw new RuntimeException("You don't have permission to view this job.");
    }

    private void validateCanViewGroupJobs(Company company) {
        if (!GroupPermission.MEMBER.equals(company.getPermission()) &&
                !GroupPermission.ADMIN.equals(company.getPermission()) &&
                !GroupPermission.OWNER.equals(company.getPermission())) {
            throw new RuntimeException("You don't have permission to view group jobs.");
        }

    }
}
