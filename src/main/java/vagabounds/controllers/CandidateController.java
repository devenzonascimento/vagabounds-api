package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vagabounds.dtos.candidate.CandidateDTO;
import vagabounds.dtos.candidate.UpdateCandidateRequest;
import vagabounds.dtos.generic.PageResult;
import vagabounds.security.SecurityUtils;
import vagabounds.services.CandidateService;
import vagabounds.services.ResumeService;

import java.util.Map;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    @Autowired
    CandidateService candidateService;

    @Autowired
    ResumeService resumeService;

    @GetMapping
    public ResponseEntity<PageResult<CandidateDTO>> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int pageSize,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "DESC") String direction
    ) {
        var candidatePage = candidateService.findAll(page, pageSize, sortBy, direction);

        Page<CandidateDTO> dtoPage = candidatePage.map(CandidateDTO::fromCandidate);

        return ResponseEntity.ok(PageResult.fromRawPage(dtoPage));
    }

    @GetMapping("/{candidateId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateDTO> findById(@PathVariable Long candidateId) {
        var candidate = candidateService.findById(candidateId);

        return ResponseEntity.ok(CandidateDTO.fromCandidate(candidate));
    }

    @GetMapping("/resume-url")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, String>> getResumeUrl() {
        var accountId = SecurityUtils.getAccountId();

        var resumeUrl = resumeService.loadCandidateResume(accountId);

        return ResponseEntity.ok(Map.of("resumeUrl", resumeUrl));
    }

    @PostMapping("/upload-resume")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, String>> uploadResume(
        @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", "File is empty"));
        }

        var accountId = SecurityUtils.getAccountId();

        resumeService.storeCandidateResume(accountId, file);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> update(@RequestBody @Valid UpdateCandidateRequest request) {
        candidateService.updateCandidate(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{candidateId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> delete(@PathVariable Long candidateId) {
        candidateService.deleteCandidateAccount(candidateId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
