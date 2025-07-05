package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.auth.RegisterCompanyRequest;
import vagabounds.dtos.company.CompanyDTO;
import vagabounds.dtos.group.*;
import vagabounds.services.GroupService;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping("/members")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<GroupMemberDTO>> getAllMembers() {
        var members = groupService.findAllMembers();

        return ResponseEntity.ok(GroupMemberDTO.fromCompanies(members));
    }

    @PostMapping("add-member")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> addMember(@RequestBody @Valid RegisterCompanyRequest request) {
        groupService.registerCompany(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("remove-member")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> removeMember(@RequestBody @Valid RemoveMemberRequest request) {
        groupService.removeMember(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<CompanyDTO>> findAll() {
        var groups = groupService.findAllMembers();

        return ResponseEntity.ok(CompanyDTO.fromCompanies(groups));
    }

    @PutMapping("/change-permission")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> changePermission(@RequestBody @Valid ChangePermissionRequest request) {
        groupService.changePermission(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
