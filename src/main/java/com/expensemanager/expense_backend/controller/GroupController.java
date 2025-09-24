package com.expensemanager.expense_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expensemanager.expense_backend.dto.GroupInviteRequest;
import com.expensemanager.expense_backend.dto.GroupRequest;
import com.expensemanager.expense_backend.model.Group;
import com.expensemanager.expense_backend.service.GroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
		super();
		this.groupService = groupService;
	}

	@PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequest req, @RequestParam String adminEmail) {
        return ResponseEntity.ok(groupService.createGroup(req.getName(), req.getDescription(), adminEmail));
    }

	@PostMapping("/invite")
	public ResponseEntity<String> inviteUser(@RequestBody GroupInviteRequest request) {
	    return ResponseEntity.ok(groupService.inviteUserToGroup(request.getGroupId(), request.getEmail()));
	}

	@GetMapping("/my-groups")
	public List<Group> getMyGroups(@RequestParam String adminEmail) {
	    System.out.println("Fetching groups for: " + adminEmail);
	    return groupService.getGroupsForUser(adminEmail);
	}


    @PostMapping("/{groupId}/accept")
    public ResponseEntity<?> acceptInvite(@PathVariable Long groupId, @RequestParam String email) {
        groupService.acceptInvite(groupId, email);
        return ResponseEntity.ok("Invite accepted");
    }
    
    @GetMapping("/pending-invitations")
    public List<Group> getPendingInvitations(@RequestParam String email) {
        return groupService.getPendingInvitations(email);
    }
    
    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<String> addMemberToGroup(
            @PathVariable Long groupId,
            @RequestBody Map<String, Object> request) {

        String email = (String) request.get("email");
        String name = (String) request.get("name");
        boolean accepted = (Boolean) request.getOrDefault("accepted", false);

        groupService.addMemberToGroup(groupId, email, name, accepted);
        return ResponseEntity.ok("User added to group successfully!");
    }
    
    @DeleteMapping("/{groupId}/decline")
    public ResponseEntity<String> declineGroupInvitation(
            @PathVariable Long groupId,
            @RequestParam String email
    ) {
        try {
            groupService.declineInvitation(groupId, email);
            return ResponseEntity.ok("Invitation declined successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to decline invitation.");
        }
    }

    
    @PostMapping("/{groupId}/join")
    public ResponseEntity<?> joinGroup(@PathVariable Long groupId, @RequestParam String email) {
        groupService.joinGroup(groupId, email);
        return ResponseEntity.ok("User joined the group successfully");
    }
    
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok("Group deleted successfully");
    }

    @DeleteMapping("/{groupId}/remove")
    public ResponseEntity<?> removeUser(@PathVariable Long groupId, @RequestParam String userEmail) {
        groupService.removeUserFromGroup(groupId, userEmail);
        return ResponseEntity.ok("User removed from group");
    }
}

