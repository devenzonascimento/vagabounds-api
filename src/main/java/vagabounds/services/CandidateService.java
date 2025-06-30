package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vagabounds.dtos.candidate.UpdateCandidateRequest;
import vagabounds.models.Candidate;
import vagabounds.repositories.AccountRepository;
import vagabounds.repositories.CandidateRepository;

@Service
public class CandidateService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CandidateRepository candidateRepository;

    public void updateCandidate(UpdateCandidateRequest request) {
        var candidate = findById(request.candidateId());

        candidate.update(
            request.name(),
            request.address(),
            request.education(),
            request.course(),
            request.semester(),
            request.graduationYear(),
            request.resumeURL()
        );

        candidateRepository.save(candidate);
    }

    public Page<Candidate> findAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return candidateRepository.findAll(pageable);
    }

    public Candidate findById(Long candidateId) {
        var candidate = candidateRepository.findById(candidateId).orElse(null);

        if (candidate == null || candidate.getIsDeleted()) {
            throw new RuntimeException("Candidate not found!");
        }

        return candidate;
    }

    public void deleteCandidateAccount(Long candidateId) {
        var candidate = findById(candidateId);
        var account = candidate.getAccount();

        candidate.setIsDeleted(true);
        account.setIsDeleted(true);

        candidateRepository.save(candidate);
        accountRepository.save(account);
    }
}
