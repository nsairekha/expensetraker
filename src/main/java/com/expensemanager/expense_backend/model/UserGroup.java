package com.expensemanager.expense_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "user_groups")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //@JsonIgnore
    private User user;

    @ManyToOne
    @JsonBackReference
    private Group group;

    private boolean accepted = false; // Invitation status

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public UserGroup(Long id, User user, Group group, boolean accepted) {
		super();
		this.id = id;
		this.user = user;
		this.group = group;
		this.accepted = accepted;
	}

	public UserGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
    // Constructors, Getters, Setters
}
