package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vagabounds.models.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
