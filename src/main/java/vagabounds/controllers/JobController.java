package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.job.CreateJobRequest;
import vagabounds.dtos.job.ExtendsExpiresAtRequest;
import vagabounds.dtos.job.JobDTO;
import vagabounds.dtos.job.UpdateJobRequest;
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
    public ResponseEntity<List<JobDTO>> findAll() {
        var jobs = jobService.findAll();

        return ResponseEntity.ok(JobDTO.fromJobs(jobs));
    }

    @GetMapping("/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobDTO> findById(@PathVariable Long jobId) {
        var job = jobService.findById(jobId);

        return ResponseEntity.ok(JobDTO.fromJob(job));
    }

    @GetMapping("/by-company")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<JobDTO>> findAllByCompany() {
        var jobs = jobService.findAllJobsByCompany();

        return ResponseEntity.ok(JobDTO.fromJobs(jobs));
    }

    @PostMapping("/extends-expires-at")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobDTO> extendsExpiresAt(@RequestBody @Valid ExtendsExpiresAtRequest request) {
        jobService.extendExpiresAt(request.jobId(), request.newExpiresAt());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

