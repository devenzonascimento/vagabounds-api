package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vagabounds.models.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
