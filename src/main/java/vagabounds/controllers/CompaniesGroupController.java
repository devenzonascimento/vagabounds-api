package vagabounds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.companiesgroup.CompaniesGroupSummary;
import vagabounds.services.CompaniesGroupService;

@RestController
@RequestMapping("/companies-group")
public class CompaniesGroupController {
    @Autowired
    CompaniesGroupService companiesGroupService;

    @PostMapping("create/{groupName}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> createGroup(@PathVariable String groupName) {
        companiesGroupService.createGroup(groupName);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<CompaniesGroupSummary> findById(@PathVariable Long groupId) {
        var group = companiesGroupService.findById(groupId);

        return ResponseEntity.ok(CompaniesGroupSummary.fromCompaniesGroup(group));
    }
}
