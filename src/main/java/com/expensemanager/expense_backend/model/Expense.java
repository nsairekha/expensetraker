//package com.expensemanager.expense_backend.model;
//
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Set;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "expenses")
//public class Expense {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Double amount;
//
//    private String description;
//
//    @Column(name = "EXPENSE_DATE")
//    private LocalDate date;
//
//    private String receiptUrl;
//
//    @ManyToOne
//    @JsonIgnore 
//    private User payer;
//
//    @ManyToOne
//    @JsonIgnore
//    private Group group;
//
//    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Set<ExpenseSplit> splits = new HashSet<>();
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public Double getAmount() {
//		return amount;
//	}
//
//	public void setAmount(Double amount) {
//		this.amount = amount;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public LocalDate getDate() {
//		return date;
//	}
//
//	public void setDate(LocalDate date) {
//		this.date = date;
//	}
//
//	public String getReceiptUrl() {
//		return receiptUrl;
//	}
//
//	public void setReceiptUrl(String receiptUrl) {
//		this.receiptUrl = receiptUrl;
//	}
//
//	public User getPayer() {
//		return payer;
//	}
//
//	public void setPayer(User payer) {
//		this.payer = payer;
//	}
//
//	public Group getGroup() {
//		return group;
//	}
//
//	public void setGroup(Group group) {
//		this.group = group;
//	}
//
//	public Set<ExpenseSplit> getSplits() {
//		return splits;
//	}
//
//	public void setSplits(Set<ExpenseSplit> splits) {
//		this.splits = splits;
//	}
//
//	public Expense(Long id, Double amount, String description, LocalDate date, String receiptUrl, User payer,
//			Group group, Set<ExpenseSplit> splits) {
//		super();
//		this.id = id;
//		this.amount = amount;
//		this.description = description;
//		this.date = date;
//		this.receiptUrl = receiptUrl;
//		this.payer = payer;
//		this.group = group;
//		this.splits = splits;
//	}
//
//	public Expense() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//
//    // Getters, setters, constructors
//}
package com.expensemanager.expense_backend.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String description;

    @Column(name = "EXPENSE_DATE")
    private LocalDate date;

    private String receiptUrl;

    @ManyToOne
    @JsonIgnore // Prevent serializing the payer in the response
    private User payer;

    @ManyToOne
    @JsonIgnore // Prevent serializing the group in the response
    private Group group;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // Eager fetching of ExpenseSplit
    @JsonManagedReference // Prevents infinite recursion during serialization
    private Set<ExpenseSplit> splits = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<ExpenseSplit> getSplits() {
        return splits;
    }

    public void setSplits(Set<ExpenseSplit> splits) {
        this.splits = splits;
    }

    // Constructors
    public Expense(Long id, Double amount, String description, LocalDate date, String receiptUrl, User payer,
                   Group group, Set<ExpenseSplit> splits) {
        super();
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.receiptUrl = receiptUrl;
        this.payer = payer;
        this.group = group;
        this.splits = splits;
    }

    public Expense() {
        super();
    }
}
