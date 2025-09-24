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

import com.expensemanager.expense_backend.dto.SettlementRequest;
import com.expensemanager.expense_backend.model.Settlement;
import com.expensemanager.expense_backend.service.SettlementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/settlements")
//@CrossOrigin(origins = "http://localhost:3000")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
		super();
		this.settlementService = settlementService;
	}

	@PostMapping("/record")
    public ResponseEntity<Settlement> settle(@RequestBody SettlementRequest request) {
        return ResponseEntity.ok(settlementService.recordSettlement(request));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Settlement>> getGroupSettlements(@PathVariable Long groupId) {
        return ResponseEntity.ok(settlementService.getSettlementsByGroup(groupId));
    }
}

