package com.expensemanager.expense_backend.dto;

import lombok.Data;

@Data
public class ExpenseSplitRequest {
    private String userEmail;
    private Double share;
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public Double getShare() {
		return share;
	}
	public void setShare(Double share) {
		this.share = share;
	}
}
