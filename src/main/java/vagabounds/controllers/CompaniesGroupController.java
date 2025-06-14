package vagabounds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vagabounds.security.SecurityUtils;
import vagabounds.services.CompaniesGroupService;

@RestController
@RequestMapping("/companies-group")
public class CompaniesGroupController {
    @Autowired
    CompaniesGroupService companiesGroupService;

    @PostMapping("create/{groupName}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> createGroup(@PathVariable String groupName) {
        Long accountId = SecurityUtils.getAccountId();

        companiesGroupService.createGroup(accountId, groupName);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
