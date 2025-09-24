package com.expensemanager.expense_backend.dto;

public class ReportResponse {
    private double totalExpenses;
    private double totalPaid;
    private double totalOwed;
    private double balance;

    public ReportResponse(double totalExpenses, double totalPaid, double totalOwed, double balance) {
        this.totalExpenses = totalExpenses;
        this.totalPaid = totalPaid;
        this.totalOwed = totalOwed;
        this.balance = balance;
    }

    // Getters and setters
    public double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(double totalExpenses) { this.totalExpenses = totalExpenses; }

    public double getTotalPaid() { return totalPaid; }
    public void setTotalPaid(double totalPaid) { this.totalPaid = totalPaid; }

    public double getTotalOwed() { return totalOwed; }
    public void setTotalOwed(double totalOwed) { this.totalOwed = totalOwed; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
