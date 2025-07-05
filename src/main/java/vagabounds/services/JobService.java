package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vagabounds.dtos.application.AppliedJobFilter;
import vagabounds.dtos.job.AppliedJobList;
import vagabounds.dtos.job.CreateJobRequest;
import vagabounds.dtos.job.ExtendsExpiresAtRequest;
import vagabounds.dtos.job.JobFilterRequest;
import vagabounds.dtos.job.UpdateJobRequest;
import vagabounds.models.Application;
import vagabounds.models.Company;
import vagabounds.models.Job;
import vagabounds.repositories.ApplicationRepository;
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

        if (!job.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("You don't have permission to edit job informations, this job is not belongs to your company");
        }

        if (!job.getIsOpen() && job.getClosedAt() != null) {
            throw new RuntimeException("This job is closed and no longer allows information updates.");
        }

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

        if (job == null || job.getIsDeleted()) {
            throw new RuntimeException("Job not found.");
        }

        return job;
    }

    public Page<Job> listJobsWithFilter(JobFilterRequest filter) {
        var candidate = getCurrentCompany();

        Specification<Job> spec = JobSpecifications.belongsToCompany(candidate.getId())
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

        return jobs.stream().filter(j -> !j.getIsDeleted()).toList();
    }

    public void extendExpiresAt(ExtendsExpiresAtRequest request) {
        var job = findById(request.jobId());

        var company = getCurrentCompany();

        if (!job.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("You don't have permission to edit job informations, this job is not belongs to your company");
        }

        job.extendExpiresAt(request.newExpiresAt());

        jobRepository.save(job);
    }

    public void closeManually(Long jobId) {
        var job = findById(jobId);

        var company = getCurrentCompany();

        if (!job.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("You don't have permission to edit job informations, this job is not belongs to your company");
        }

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

    public List<AppliedJobList> findAllAppliedJobs(AppliedJobFilter filter) {

        Specification<Application> spec = Specification.anyOf(
                ApplicationSpecifications.hasCandidateId(filter.candidateId())
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
}
