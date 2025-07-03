package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vagabounds.dtos.job.CreateJobRequest;
import vagabounds.dtos.job.ExtendsExpiresAtRequest;
import vagabounds.dtos.job.UpdateJobRequest;
import vagabounds.models.Company;
import vagabounds.models.Job;
import vagabounds.repositories.CompanyRepository;
import vagabounds.repositories.JobRepository;
import vagabounds.security.SecurityUtils;

import java.util.List;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    CompanyRepository companyRepository;

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

    public List<Job> findAll() {
        var jobs = jobRepository.findAll();

        if (jobs.isEmpty()) {
            return jobs;
        }

        return jobs.stream().filter(j -> !j.getIsDeleted()).toList();
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

    private Company getCurrentCompany() {
        var accountId = SecurityUtils.getAccountId();

        var company = companyRepository.findByAccountId(accountId).orElse(null);

        if (company == null || company.getIsDeleted()) {
            throw new RuntimeException("Company not found.");
        }

        return company;
    }
}
