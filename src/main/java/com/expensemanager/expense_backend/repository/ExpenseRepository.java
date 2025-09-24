package com.expensemanager.expense_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expensemanager.expense_backend.model.Expense;
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Get all expenses for a group
    List<Expense> findByGroupId(Long groupId);

    // Get all expenses created by a specific user
    @Query("SELECT e FROM Expense e WHERE e.payer.id = :userId")
    List<Expense> findByUserId(@Param("userId") Long userId);
    
 // This is likely already in your repository, but adding for completeness
    Optional<Expense> findById(Long id);
    
    @Query("SELECT e FROM Expense e JOIN FETCH e.payer JOIN FETCH e.group WHERE e.id = :expenseId")
    Optional<Expense> findByIdWithDetails(@Param("expenseId") Long expenseId);
}