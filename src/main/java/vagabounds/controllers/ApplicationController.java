package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vagabounds.dtos.application.ApplyToJobRequest;
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
}
