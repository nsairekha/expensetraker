package com.expensemanager.expense_backend.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;

import com.expensemanager.expense_backend.model.Expense;

public class ExcelExporter {

    private final List<Expense> expenses;

    public ExcelExporter(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public byte[] exportToByteArray() throws IOException {
        try (
            HSSFWorkbook workbook = new HSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            HSSFSheet sheet = workbook.createSheet("Expense Report");

            HSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Amount");
            header.createCell(2).setCellValue("Description");

            int rowCount = 1;
            for (Expense e : expenses) {
                HSSFRow row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(e.getDate().toString());
                row.createCell(1).setCellValue(e.getAmount().doubleValue());
                row.createCell(2).setCellValue(e.getDescription());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
