package com.expensemanager.expense_backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensemanager.expense_backend.dto.ReportResponse;
import com.expensemanager.expense_backend.model.Expense;
import com.expensemanager.expense_backend.model.ExpenseSplit;
import com.expensemanager.expense_backend.model.User;
import com.expensemanager.expense_backend.repository.ExpenseRepository;
import com.expensemanager.expense_backend.repository.ExpenseSplitRepository;
import com.expensemanager.expense_backend.repository.GroupRepository;
import com.expensemanager.expense_backend.repository.UserRepository;
import com.expensemanager.expense_backend.util.ExcelExporter;
import com.expensemanager.expense_backend.util.PdfGenerator;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
public class ReportService {

    private final ExpenseRepository expenseRepository;
	private final GroupRepository groupRepository;
	@Autowired
	private ExpenseSplitRepository expenseSplitRepository;

	@Autowired
	private UserRepository userRepository;

    public ReportService(ExpenseRepository expenseRepository, GroupRepository groupRepository) {
		super();
		this.expenseRepository = expenseRepository;
		this.groupRepository = groupRepository;
	}

    public List<Expense> getGroupReport(Long groupId) {
        return expenseRepository.findByGroupId(groupId);
    }

    public ReportResponse getUserReport(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        List<Expense> userExpenses = expenseRepository.findByUserId(userId);
        List<ExpenseSplit> splits = expenseSplitRepository.findByUser(user);

        double totalExpenses = 0;
        double totalPaid = 0;
        double totalOwed = 0;

        // Calculate total expenses and total paid
        for (Expense expense : userExpenses) {
            totalExpenses += expense.getAmount();

            if (expense.getPayer().getId().equals(userId)) {
                totalPaid += expense.getAmount();
            }
        }

        // Calculate total owed from splits (using share)
        for (ExpenseSplit split : splits) {
            totalOwed += split.getShare(); // Adjusted for your field name
        }

        double balance = totalPaid - totalOwed;

        return new ReportResponse(totalExpenses, totalPaid, totalOwed, balance);
    }

    public byte[] exportGroupReportToExcel(Long groupId) throws IOException {
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        ExcelExporter exporter = new ExcelExporter(expenses);
        return exporter.exportToByteArray();
    }


    public void exportGroupReportToPdf(Long groupId, HttpServletResponse response) throws IOException {
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        new PdfGenerator(expenses).generate(response);
    }
}

