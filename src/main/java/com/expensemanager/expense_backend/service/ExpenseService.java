package com.expensemanager.expense_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.expensemanager.expense_backend.dto.ExpenseRequest;
import com.expensemanager.expense_backend.dto.ExpenseSplitRequest;
import com.expensemanager.expense_backend.model.Expense;
import com.expensemanager.expense_backend.model.ExpenseSplit;
import com.expensemanager.expense_backend.model.Group;
import com.expensemanager.expense_backend.model.User;
import com.expensemanager.expense_backend.repository.ExpenseRepository;
import com.expensemanager.expense_backend.repository.ExpenseSplitRepository;
import com.expensemanager.expense_backend.repository.GroupRepository;
import com.expensemanager.expense_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepo;
    private final ExpenseSplitRepository splitRepo;
    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final EmailService emailService;
    public ExpenseService(ExpenseRepository expenseRepo, ExpenseSplitRepository splitRepo, UserRepository userRepo,
			GroupRepository groupRepo, EmailService emailService) {
		super();
		this.expenseRepo = expenseRepo;
		this.splitRepo = splitRepo;
		this.userRepo = userRepo;
		this.groupRepo = groupRepo;
		this.emailService = emailService;
	}

    public Expense addExpense(ExpenseRequest request) {
        User payer = userRepo.findByEmail(request.getPayerEmail()).orElseThrow();
        Group group = groupRepo.findById(request.getGroupId()).orElseThrow();
        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setDate(request.getDate());
        expense.setReceiptUrl(request.getReceiptUrl());
        expense.setPayer(payer);
        expense.setGroup(group);
        Expense savedExpense = expenseRepo.save(expense);
        
        for (ExpenseSplitRequest splitRequest : request.getSplits()) {
            User user = userRepo.findByEmail(splitRequest.getUserEmail()).orElseThrow();
            ExpenseSplit split = new ExpenseSplit();
            split.setUser(user);
            split.setExpense(savedExpense);
            split.setShare(splitRequest.getShare());
            splitRepo.save(split);
        
            if (!user.getEmail().equalsIgnoreCase(payer.getEmail())) {
                // Prepare flattened template variables
                java.util.Map<String, Object> variables = new java.util.HashMap<>();
                // User variables
                variables.put("userName", user.getName() != null ? user.getName() : user.getEmail());
                variables.put("userEmail", user.getEmail());
                
                // Payer variables
                variables.put("payerName", payer.getName() != null ? payer.getName() : payer.getEmail());
                variables.put("payerEmail", payer.getEmail());
                
                // Expense variables
                variables.put("expenseDescription", expense.getDescription());
                variables.put("expenseAmount", expense.getAmount());
                variables.put("expenseDate", expense.getDate());
                
                // Group variables
                variables.put("groupName", group.getName());
                
                // Split variable
                variables.put("share", splitRequest.getShare());
                
                emailService.sendEmail(
                    user.getEmail(),
                    "New Expense Added in Group " + group.getName(),
                    "added-expense",
                    variables
                );
            }
        }
        return savedExpense;
    }
    
    public void notifyUsersAboutExpense(Long expenseId) {
        Expense expense = expenseRepo.findById(expenseId)
            .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        User payer = expense.getPayer();
        if (payer == null) {
            throw new RuntimeException("Payer information missing for expense");
        }
        
        Group group = expense.getGroup();
        if (group == null) {
            throw new RuntimeException("Group information missing for expense");
        }
        
        List<ExpenseSplit> splits = splitRepo.findByExpenseId(expenseId);
        
        for (ExpenseSplit split : splits) {
            User user = split.getUser();
            if (user == null || user.getEmail() == null) {
                continue; // Skip if user info is incomplete
            }
            
            // Skip notifying the payer
            if (user.getId().equals(payer.getId())) {
                continue;
            }
            
            // Prepare flattened template variables
            java.util.Map<String, Object> variables = new java.util.HashMap<>();
            // User variables
            variables.put("userName", user.getName() != null ? user.getName() : user.getEmail());
            variables.put("userEmail", user.getEmail());
            
            // Payer variables
            variables.put("payerName", payer.getName() != null ? payer.getName() : payer.getEmail());
            variables.put("payerEmail", payer.getEmail());
            
            // Expense variables
            variables.put("expenseDescription", expense.getDescription());
            variables.put("expenseAmount", expense.getAmount());
            variables.put("expenseDate", expense.getDate());
            
            // Group variables
            variables.put("groupName", group.getName());
            
            // Split variable
            variables.put("share", split.getShare());
            
            try {
                emailService.sendEmail(
                    user.getEmail(),
                    "Expense Reminder: " + expense.getDescription() + " in " + group.getName(),
                    "notify-expense",
                    variables
                );
            } catch (Exception e) {
                System.err.println("Error sending notification to " + user.getEmail() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public List<Expense> getGroupExpenses(Long groupId) {
        groupRepo.findById(groupId).orElseThrow(); // just to validate group exists
        return expenseRepo.findByGroupId(groupId);
    }
   
}
