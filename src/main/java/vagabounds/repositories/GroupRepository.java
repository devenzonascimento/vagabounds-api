package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vagabounds.models.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
