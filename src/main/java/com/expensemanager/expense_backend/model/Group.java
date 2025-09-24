package com.expensemanager.expense_backend.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

//@Entity
//@Table(name = "`groups`")
//public class Group {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    private String description;
//
//    @ManyToOne
//    @JoinColumn(name = "admin_id")
//    private User admin;
//
//    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Set<UserGroup> members = new HashSet<>();
//    
//    @Column(name = "created_at", updatable = false, insertable = false)
//    private Timestamp createdAt;
//
//    public Timestamp getCreatedAt() {
//        return createdAt;
//    }
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
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
//	public User getAdmin() {
//		return admin;
//	}
//
//	public void setAdmin(User admin) {
//		this.admin = admin;
//	}
//
//	public Set<UserGroup> getMembers() {
//		return members;
//	}
//
//	public void setMembers(Set<UserGroup> members) {
//		this.members = members;
//	}
//
//	public Group(Long id, String name, String description, User admin, Set<UserGroup> members) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.description = description;
//		this.admin = admin;
//		this.members = members;
//	}
//
//	public Group() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//    // Constructors, Getters, Setters
//}
@Entity
@Table(name = "`groups`")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<UserGroup> members = new HashSet<>();

    @Column(name = "created_at", updatable = false, insertable = false)
    private Timestamp createdAt;

    // Constructors, Getters, and Setters
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Set<UserGroup> getMembers() {
        return members;
    }

    public void setMembers(Set<UserGroup> members) {
        this.members = members;
    }

    public Group(Long id, String name, String description, User admin, Set<UserGroup> members) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.admin = admin;
        this.members = members;
    }

    public Group() {
        super();
    }
}
