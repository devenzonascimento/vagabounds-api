package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.candidate.CandidateDTO;
import vagabounds.dtos.candidate.UpdateCandidateRequest;
import vagabounds.dtos.generic.PageResult;
import vagabounds.services.CandidateService;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    @Autowired
    CandidateService candidateService;

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
