package com.expensemanager.expense_backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expensemanager.expense_backend.model.Group;
import com.expensemanager.expense_backend.model.User;
import com.expensemanager.expense_backend.model.UserGroup;
import com.expensemanager.expense_backend.repository.GroupRepository;
import com.expensemanager.expense_backend.repository.UserGroupRepository;
import com.expensemanager.expense_backend.repository.UserRepository;
import com.expensemanager.expense_backend.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
public class GroupService {

    public GroupService(GroupRepository groupRepo, UserRepository userRepo, UserGroupRepository userGroupRepo,
			EmailService emailService) {
		super();
		this.groupRepo = groupRepo;
		this.userRepo = userRepo;
		this.userGroupRepo = userGroupRepo;
		this.emailService = emailService;
	}

	private final GroupRepository groupRepo;
    private final UserRepository userRepo;
    private final UserGroupRepository userGroupRepo;
    private final EmailService emailService;

    public Group createGroup(String name, String desc, String adminEmail) {
        User admin = userRepo.findByEmail(adminEmail).orElseThrow();
        Group group = new Group();
        group.setName(name);
        group.setDescription(desc);
        group.setAdmin(admin);
        return groupRepo.save(group);
    }

    public String inviteUserToGroup(Long groupId, String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Group group = groupRepo.findById(groupId).orElseThrow();

        if (userGroupRepo.findByUserAndGroup(user, group).isPresent())
            return "User already invited";

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroup.setAccepted(false);
        userGroupRepo.save(userGroup);

        // Send email using HTML template
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        variables.put("group", group);
        variables.put("inviteLink", "http://localhost:3000/accept-invite?groupId=" + groupId + "&email=" + email);
        emailService.sendEmail(
                user.getEmail(),
                "You're invited to join a group!",
                "group-invite",
                variables
        );

        return "User invited to group";
    }

    public List<Group> getGroupsForUser(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return userGroupRepo.findByUser(user).stream()
                .filter(UserGroup::isAccepted)
                .map(UserGroup::getGroup)
                .collect(Collectors.toList());
    }

    public void acceptInvite(Long groupId, String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Group group = groupRepo.findById(groupId).orElseThrow();

        UserGroup userGroup = userGroupRepo.findByUserAndGroup(user, group)
                .orElseThrow(() -> new RuntimeException("Pending invitation not found"));
        userGroup.setAccepted(true);
        userGroupRepo.save(userGroup);
    }
    
    public void addMemberToGroup(Long groupId, String email, String name, boolean accepted) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepo.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));

        if (userGroupRepo.findByUserAndGroup(user, group).isPresent()) {
            throw new RuntimeException("User already in the group");
        }

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroup.setAccepted(accepted);
        userGroupRepo.save(userGroup);
    }
    
    /**
     * New method: Join group directly.
     * If a pending invitation exists, mark it accepted.
     * Otherwise, add the user directly as a member.
     */
    public void joinGroup(Long groupId, String email) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if a pending invitation exists
        var existingInvite = userGroupRepo.findByUserAndGroup(user, group);
        if (existingInvite.isPresent()) {
            UserGroup userGroup = existingInvite.get();
            if (userGroup.isAccepted()) {
                throw new RuntimeException("User is already a member of the group");
            } else {
                // Accept the pending invitation
                userGroup.setAccepted(true);
                userGroupRepo.save(userGroup);
                return;
            }
        }
        
        // No pending invite: add user directly as an accepted member
        UserGroup newMembership = new UserGroup();
        newMembership.setUser(user);
        newMembership.setGroup(group);
        newMembership.setAccepted(true);
        userGroupRepo.save(newMembership);
    }
    
    public void declineInvitation(Long groupId, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Optional<UserGroup> userGroupOptional = userGroupRepo.findByUserAndGroup(user, group);

        if (userGroupOptional.isPresent()) {
            userGroupRepo.delete(userGroupOptional.get());
        } else {
            throw new RuntimeException("Invitation not found for this user and group.");
        }
    }


    public List<Group> getPendingInvitations(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return userGroupRepo.findByUser(user).stream()
                .filter(userGroup -> !userGroup.isAccepted())
                .map(UserGroup::getGroup)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeUserFromGroup(Long groupId, String userEmail) {
        User user = userRepo.findByEmail(userEmail).orElseThrow();
        Group group = groupRepo.findById(groupId).orElseThrow();

        UserGroup userGroup = userGroupRepo.findByUserAndGroup(user, group)
                .orElseThrow(() -> new RuntimeException("User is not a member of the group"));
        userGroupRepo.delete(userGroup);
    }
    
    @Transactional
    public void deleteGroup(Long groupId) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        // First delete all user-group associations
        userGroupRepo.deleteByGroup(group);
        
        // Then delete the group itself
        groupRepo.delete(group);
    }
}
