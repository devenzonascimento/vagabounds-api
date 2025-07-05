package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vagabounds.models.Application;

import java.util.Optional;
public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {
    Optional<Application> findByJobIdAndCandidateId(Long jobId, Long candidateId);
}
