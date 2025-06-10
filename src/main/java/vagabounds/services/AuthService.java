package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vagabounds.dtos.auth.RegisterCompanyRequest;
import vagabounds.models.Account;
import vagabounds.models.Company;
import vagabounds.repositories.AccountRepository;
import vagabounds.repositories.CompanyRepository;
import vagabounds.security.Role;

import java.util.HashSet;
import java.util.List;

@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PasswordEncoder encoder;

    @Transactional
    public void registerCompany(RegisterCompanyRequest request) {
        Account accountWithSameEmail = accountRepository.findByEmail(request.email()).orElse(null);

        if (accountWithSameEmail != null) {
            throw new RuntimeException("Failed to register company account, already exists an account with the same email.");
        }

        Company companyWithSameCnpj = companyRepository.findByCnpj(request.cnpj()).orElse(null);

        if (companyWithSameCnpj != null) {
            throw new RuntimeException("Failed to register company account, already exists a company with the same cnpj.");
        }

        Account account = new Account();
        account.setEmail(request.email());
        account.setPassword(encoder.encode(request.password()));
        account.setRoles(new HashSet<>(List.of(Role.ROLE_COMPANY)));

        account = accountRepository.save(account);

        Company company = new Company();
        company.setName(request.name());
        company.setCnpj(request.cnpj());
        company.setAddress(request.address());
        company.setAccount(account);

        companyRepository.save(company);
    }
}
