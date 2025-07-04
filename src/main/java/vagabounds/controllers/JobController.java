package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.generic.PageResult;
import vagabounds.dtos.job.*;
import vagabounds.services.JobService;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {
    @Autowired
    JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> createJob(@RequestBody @Valid CreateJobRequest request) {
        jobService.createJob(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> updateJob(@RequestBody @Valid UpdateJobRequest request) {
        jobService.updateJob(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<PageResult<JobDTO>> list(JobFilterRequest filter) {
        var jobsPage = jobService.listJobsWithFilter(filter);

        Page<JobDTO> dtoPage = jobsPage.map(JobDTO::fromJob);

        return ResponseEntity.ok(PageResult.fromRawPage(dtoPage));
    }

    @GetMapping("/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobDetailsReponse> findById(@PathVariable Long jobId) {
        var job = jobService.findById(jobId);

        return ResponseEntity.ok(JobDetailsReponse.fromJob(job));
    }

    @GetMapping("/by-company")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<JobDTO>> findAllByCompany() {
        var jobs = jobService.findAllJobsByCompany();

        return ResponseEntity.ok(JobDTO.fromJobs(jobs));
    }

    @PostMapping("/extend")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobDTO> extendsExpiresAt(@RequestBody @Valid ExtendsExpiresAtRequest request) {
        jobService.extendExpiresAt(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/close/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobDTO> closeManually(@PathVariable Long jobId) {
        jobService.closeManually(jobId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
