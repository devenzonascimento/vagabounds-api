package vagabounds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vagabounds.models.Candidate;
import vagabounds.repositories.CandidateRepository;
import vagabounds.security.SecurityUtils;
import vagabounds.services.ResumeService;

import java.util.Map;

// TODO: Talvez remover esse controller e jogar tudo pro CandidateController
@RestController
@RequestMapping("/resume")
public class ResumeController {
    @Autowired
    ResumeService resumeService;

    @Autowired
    CandidateRepository candidateRepository;

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, String>> uploadResume(
        @RequestParam("file") MultipartFile file
    ) {
        var candidate = getCurrentCandidate();

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "File is empty"));
        }

        resumeService.storeCandidateResume(candidate, file);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, String>> getResumeUrl() {
        var candidate = getCurrentCandidate();

        var resumeUrl = resumeService.loadCandidateResume(candidate);

        return ResponseEntity.ok(Map.of("resumeUrl", resumeUrl));
    }

    private Candidate getCurrentCandidate() {
        var accountId = SecurityUtils.getAccountId();

        var candidate = candidateRepository.findByAccountId(accountId).orElse(null);

        if (candidate == null || candidate.getIsDeleted()) {
            throw new RuntimeException("Candidate not found.");
        }

        return candidate;
    }
}
