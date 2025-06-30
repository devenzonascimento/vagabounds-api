package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vagabounds.dtos.auth.RegisterCandidateRequest;
import vagabounds.dtos.auth.RegisterCompanyRequest;
import vagabounds.models.Account;
import vagabounds.models.Candidate;
import vagabounds.models.Company;
import vagabounds.repositories.AccountRepository;
import vagabounds.repositories.CandidateRepository;
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
    CandidateRepository candidateRepository;

    @Autowired
    PasswordEncoder encoder;

    @Transactional
    public void registerCompany(RegisterCompanyRequest request) {
        var accountWithSameEmail = accountRepository.findByEmail(request.email()).orElse(null);

        if (accountWithSameEmail != null) {
            throw new RuntimeException("Failed to register account, already exists an account with the same email.");
        }

        Company companyWithSameCnpj = companyRepository.findByCnpj(request.cnpj()).orElse(null);

        if (companyWithSameCnpj != null) {
            throw new RuntimeException("Failed to register company account, already exists a company with the same cnpj.");
        }

        Company company = new Company();
        company.setName(request.name());
        company.setCnpj(request.cnpj());
        company.setAddress(request.address());

        Account account = createAccount(request.email(), request.password(), Role.ROLE_COMPANY);
        company.setAccount(account);

        companyRepository.save(company);
    }

    @Transactional
    public void registerCandidate(RegisterCandidateRequest request) {
        var accountWithSameEmail = accountRepository.findByEmail(request.email()).orElse(null);

        if (accountWithSameEmail != null) {
            throw new RuntimeException("Failed to register account, already exists an account with the same email.");
        }

        var account = createAccount(request.email(), request.password(), Role.ROLE_CANDIDATE);

        var candidate = new Candidate(
            account,
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

    @Transactional
    private Account createAccount(String email, String password, Role role) {
        Account accountWithSameEmail = accountRepository.findByEmail(email).orElse(null);

        if (accountWithSameEmail != null) {
            throw new RuntimeException("Failed to register candidate account, already exists an account with the same email.");
        }

        Account account = new Account();
        account.setEmail(email);
        account.setPassword(encoder.encode(password));
        account.setRoles(new HashSet<>(List.of(role)));

        return accountRepository.save(account);
    }
}
