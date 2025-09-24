package com.expensemanager.expense_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensemanager.expense_backend.model.Group;
import com.expensemanager.expense_backend.model.User;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByAdmin(User admin);
}
