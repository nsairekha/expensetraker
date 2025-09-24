package com.expensemanager.expense_backend.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.expensemanager.expense_backend.dto.SettlementRequest;
import com.expensemanager.expense_backend.model.Group;
import com.expensemanager.expense_backend.model.Settlement;
import com.expensemanager.expense_backend.model.User;
import com.expensemanager.expense_backend.repository.GroupRepository;
import com.expensemanager.expense_backend.repository.SettlementRepository;
import com.expensemanager.expense_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
public class SettlementService {

	private final SettlementRepository settlementRepo;
    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final EmailService emailService;

    public SettlementService(SettlementRepository settlementRepo, UserRepository userRepo, GroupRepository groupRepo,
			EmailService emailService) {
		super();
		this.settlementRepo = settlementRepo;
		this.userRepo = userRepo;
		this.groupRepo = groupRepo;
		this.emailService = emailService;
	}

	public Settlement recordSettlement(SettlementRequest request) {
        User payer = userRepo.findByEmail(request.getPayerEmail()).orElseThrow();
        User payee = userRepo.findByEmail(request.getPayeeEmail()).orElseThrow();
        Group group = groupRepo.findById(request.getGroupId()).orElseThrow();

        // Adjust balances
        payer.setBalance(payer.getBalance() + request.getAmount());
        payee.setBalance(payee.getBalance() - request.getAmount());

        userRepo.save(payer);
        userRepo.save(payee);

        // Record settlement
        Settlement s = new Settlement();
        s.setAmount(request.getAmount());
        s.setDate(LocalDateTime.now());
        s.setPayer(payer);
        s.setPayee(payee);
        s.setGroup(group);

        // Send HTML email
        Map<String, Object> variables = new HashMap<>();
        variables.put("payer", payer);
        variables.put("payee", payee);
        variables.put("amount", request.getAmount());
        variables.put("group", group);

        emailService.sendEmail(
                payee.getEmail(),
                "Settlement Received",
                "settlement-notification",
                variables
        );

        return settlementRepo.save(s);
    }

    public List<Settlement> getSettlementsByGroup(Long groupId) {
        Group group = groupRepo.findById(groupId).orElseThrow();
        return settlementRepo.findByGroup(group);
    }
}

