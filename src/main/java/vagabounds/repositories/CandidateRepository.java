package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vagabounds.models.Candidate;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByCpf(String cpf);
}
