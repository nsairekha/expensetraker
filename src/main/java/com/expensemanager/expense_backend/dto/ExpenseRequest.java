package com.expensemanager.expense_backend.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ExpenseRequest {
    private Double amount;
    private String description;
    private LocalDate date;
    private String payerEmail;
    private Long groupId;
    private List<ExpenseSplitRequest> splits;
    private String receiptUrl; // Optional, can be a URL or null
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getPayerEmail() {
		return payerEmail;
	}
	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public List<ExpenseSplitRequest> getSplits() {
		return splits;
	}
	public void setSplits(List<ExpenseSplitRequest> splits) {
		this.splits = splits;
	}
	public String getReceiptUrl() {
		return receiptUrl;
	}
	public void setReceiptUrl(String receiptUrl) {
		this.receiptUrl = receiptUrl;
	}
    
    
}
