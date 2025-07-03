package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.application.ApplyToJobRequest;
import vagabounds.dtos.application.ApproveCandidateRequest;
import vagabounds.dtos.application.RejectCandidateRequest;
import vagabounds.services.ApplicationService;

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
