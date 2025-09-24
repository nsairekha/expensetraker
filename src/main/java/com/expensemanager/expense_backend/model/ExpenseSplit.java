package com.expensemanager.expense_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

//@Entity
//@Table(name = "expense_splits")
//public class ExpenseSplit {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "EXPENSE_SHARE", nullable = false)
//    private Double share;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "expense_id")
//    @JsonBackReference
//    private Expense expense;
//
//    public ExpenseSplit() {}
//
//    public ExpenseSplit(Long id, Double share, User user, Expense expense) {
//        this.id = id;
//        this.share = share;
//        this.user = user;
//        this.expense = expense;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Double getShare() {
//        return share;
//    }
//
//    public void setShare(Double share) {
//        this.share = share;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public Expense getExpense() {
//        return expense;
//    }
//
//    public void setExpense(Expense expense) {
//        this.expense = expense;
//    }
//}

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "expense_splits")
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EXPENSE_SHARE", nullable = false)
    private Double share;

    @ManyToOne(fetch = FetchType.EAGER)  // EAGER load user
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)  // EAGER load expense
    @JoinColumn(name = "expense_id")
    @JsonBackReference  // Prevents infinite recursion during JSON serialization
    private Expense expense;

    public ExpenseSplit() {}

    public ExpenseSplit(Long id, Double share, User user, Expense expense) {
        this.id = id;
        this.share = share;
        this.user = user;
        this.expense = expense;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getShare() {
        return share;
    }

    public void setShare(Double share) {
        this.share = share;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }
}
