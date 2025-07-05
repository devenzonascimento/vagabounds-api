package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vagabounds.dtos.application.ApplyToJobRequest;
import vagabounds.dtos.application.ApproveCandidateRequest;
import vagabounds.dtos.application.RejectCandidateRequest;
import vagabounds.services.ApplicationService;
import vagabounds.services.JobService;
import java.util.Map;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;
    
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
