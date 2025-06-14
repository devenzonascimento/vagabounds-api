package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vagabounds.models.CompaniesGroup;

public interface CompaniesGroupRepository extends JpaRepository<CompaniesGroup, Long> {
}
