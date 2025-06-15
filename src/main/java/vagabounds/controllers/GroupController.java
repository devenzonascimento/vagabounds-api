package vagabounds.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vagabounds.dtos.group.AddMemberRequest;
import vagabounds.dtos.group.GroupCompaniesSummary;
import vagabounds.dtos.group.RemoveMemberRequest;
import vagabounds.services.GroupService;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupService groupService;

    @PostMapping("create/{groupName}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> createGroup(@PathVariable String groupName) {
        groupService.createGroup(groupName);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("add-member")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> addMember(@RequestBody @Valid AddMemberRequest request) {
        groupService.addMember(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("remove-member")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> removeMember(@RequestBody @Valid RemoveMemberRequest request) {
        groupService.removeMember(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<GroupCompaniesSummary> findById(@PathVariable Long groupId) {
        var group = groupService.findById(groupId);

        return ResponseEntity.ok(GroupCompaniesSummary.fromGroup(group));
    }
}
