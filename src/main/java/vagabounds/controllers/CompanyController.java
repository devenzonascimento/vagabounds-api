package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.company.CompanyDTO;
import vagabounds.dtos.company.UpdateCompanyRequest;
import vagabounds.services.CompanyService;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @GetMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyDTO> getCurrentCompany() {
        var company = companyService.getCurrentCompanyInfo();

        return ResponseEntity.ok(CompanyDTO.fromCompany(company));
    }

    @GetMapping("/{companyId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyDTO> findById(@PathVariable Long companyId) {
        var company = companyService.findById(companyId);

        return ResponseEntity.ok(CompanyDTO.fromCompany(company));
    }

    @PutMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> updateCompany(@RequestBody @Valid UpdateCompanyRequest request) {
        companyService.updateCompany(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> deleteCompany() {
        companyService.deleteCompany();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}