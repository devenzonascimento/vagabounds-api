package vagabounds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vagabounds.models.Company;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCnpj(String cnpj);
}
