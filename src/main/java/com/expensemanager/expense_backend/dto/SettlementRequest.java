package com.expensemanager.expense_backend.dto;

import lombok.Data;

@Data
public class SettlementRequest {
    private String payerEmail;
    private String payeeEmail;
    private Long groupId;
    private Double amount;
	public String getPayerEmail() {
		return payerEmail;
	}
	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}
	public String getPayeeEmail() {
		return payeeEmail;
	}
	public void setPayeeEmail(String payeeEmail) {
		this.payeeEmail = payeeEmail;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
