package com.expensemanager.expense_backend.controller;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensemanager.expense_backend.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
		super();
		this.reportService = reportService;
	}

	@GetMapping("/group/{groupId}")
    public ResponseEntity<?> getGroupReport(@PathVariable Long groupId) {
        return ResponseEntity.ok(reportService.getGroupReport(groupId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReport(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getUserReport(userId));
    }

    @GetMapping("/group/{groupId}/export/excel")
    public ResponseEntity<ByteArrayResource> exportGroupReportExcel(@PathVariable Long groupId) throws IOException {
        byte[] excelData = reportService.exportGroupReportToExcel(groupId);
        ByteArrayResource resource = new ByteArrayResource(excelData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=group_report.xls")
                .contentLength(excelData.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }

    
    @GetMapping("/group/{groupId}/export/pdf")
    public void exportGroupReportPdf(@PathVariable Long groupId, HttpServletResponse response) throws IOException {
        reportService.exportGroupReportToPdf(groupId, response);
    }
}

