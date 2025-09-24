package com.expensemanager.expense_backend.dto;

public class GroupInviteRequest {
    private Long groupId;
    private String email;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    // getters and setters
}

