package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.application.AppliedJobFilter;
import vagabounds.dtos.candidate.UpdateCandidateRequest;
import vagabounds.dtos.generic.PageResult;
import vagabounds.dtos.job.*;
import vagabounds.enums.ApplicationStatus;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;
import vagabounds.services.FeedService;
import vagabounds.services.JobService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {
    @Autowired
    JobService jobService;

    @Autowired
    FeedService feedService;

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
    public ResponseEntity<PageResult<JobDTO>> list(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int pageSize,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "DESC") String direction,
        @RequestParam(required = false) Boolean isOpen,
        @RequestParam(required = false) LocalDate createdFrom,
        @RequestParam(required = false) LocalDate createdTo
    ) {
        JobFilterRequest filter = new JobFilterRequest(
            page, pageSize, sortBy, direction, isOpen, createdFrom, createdTo
        );

        var jobsPage = jobService.listJobsWithFilter(filter);

        Page<JobDTO> dtoPage = jobsPage.map(JobDTO::fromJob);

        return ResponseEntity.ok(PageResult.fromRawPage(dtoPage));
    }

    @GetMapping("/feed")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<PageResult<JobFeedDTO>> feed(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int pageSize,
        @RequestParam List<String> skills
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);

        var result = feedService.getFeed(skills, pageable);

        return ResponseEntity.ok(PageResult.fromRawPage(result));
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

    @GetMapping("/applied-jobs")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<AppliedJobList>> findAppliedJobs(
        @RequestParam Long candidateId,
        @RequestParam(required = false) ApplicationStatus status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appliedAt,
        @RequestParam(required = false) JobType jobType,
        @RequestParam(required = false) JobModality jobModality
    ) {
        var filter = new AppliedJobFilter(candidateId, status, appliedAt, jobType, jobModality);
        return ResponseEntity.ok(jobService.findAllAppliedJobs(filter));
    }

    @GetMapping("/candidate-information/{jobId}/{candidateId}")
    @PreAuthorize("hasRole('COMPANY') or hasRole('GROUP')")
    public ResponseEntity<UpdateCandidateRequest> candidateInformationAndResume (
        @PathVariable Long jobId,
        @PathVariable Long candidateId
    )
    {
        var candidateInformation = jobService.candidateInformationAndResume(jobId, candidateId);
        return ResponseEntity.ok(candidateInformation);
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
