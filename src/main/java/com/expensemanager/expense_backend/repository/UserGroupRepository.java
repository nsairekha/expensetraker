package com.expensemanager.expense_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensemanager.expense_backend.model.Group;
import com.expensemanager.expense_backend.model.User;
import com.expensemanager.expense_backend.model.UserGroup;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByUser(User user);
    List<UserGroup> findByGroup(Group group);
    Optional<UserGroup> findByUserAndGroup(User user, Group group);
    void deleteByGroup(Group group);
}

