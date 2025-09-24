package com.expensemanager.expense_backend.controller;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensemanager.expense_backend.dto.ExpenseRequest;
import com.expensemanager.expense_backend.model.Expense;
import com.expensemanager.expense_backend.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
		super();
		this.expenseService = expenseService;
	}

	@PostMapping("/add")
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.addExpense(request));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Expense>> getGroupExpenses(@PathVariable Long groupId) {
        return ResponseEntity.ok(expenseService.getGroupExpenses(groupId));
    }
    
    @PostMapping("/notify/{expenseId}")
    public ResponseEntity<String> notifyUsers(@PathVariable Long expenseId) {
        expenseService.notifyUsersAboutExpense(expenseId);
        return ResponseEntity.ok("Notifications sent successfully");
    }
}

