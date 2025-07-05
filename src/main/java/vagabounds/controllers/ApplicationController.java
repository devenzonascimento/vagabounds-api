package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vagabounds.dtos.application.AppliedJobFilter;
import vagabounds.dtos.application.ApplyToJobRequest;
import vagabounds.dtos.application.ApproveCandidateRequest;
import vagabounds.dtos.application.RejectCandidateRequest;
import vagabounds.dtos.job.AppliedJobList;
import vagabounds.enums.ApplicationStatus;
import vagabounds.enums.JobModality;
import vagabounds.enums.JobType;
import vagabounds.security.SecurityUtils;
import vagabounds.services.ApplicationService;
import vagabounds.services.JobService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;

    @Autowired
    JobService jobService;

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

    @PostMapping("/apply-to")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> applyTo(@RequestBody @Valid ApplyToJobRequest request) {
        applicationService.applyToJob(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/apply-to/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, String>> applyWithResume(
        @PathVariable Long jobId,
        @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", "File is empty"));
        }

        applicationService.applyWithResume(jobId, file);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> approveCandidate(@RequestBody @Valid ApproveCandidateRequest request) {
        applicationService.approveCandidate(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/reject")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> rejectCandidate(@RequestBody @Valid RejectCandidateRequest request) {
        applicationService.rejectCandidate(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
