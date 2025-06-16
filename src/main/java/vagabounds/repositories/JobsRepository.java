package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vagabounds.models.Jobs;

public interface JobsRepository extends JpaRepository<Jobs, Long> {
}
