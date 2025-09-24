package com.expensemanager.expense_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensemanager.expense_backend.model.ExpenseSplit;
import com.expensemanager.expense_backend.model.User;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    List<ExpenseSplit> findByUser(User user);
    List<ExpenseSplit> findByExpenseId(Long expenseId);
}
