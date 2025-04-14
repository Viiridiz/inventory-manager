package com.example.inventory_manager.model;

import java.time.LocalDate;

public class Report implements ReportGeneratable {
    private String reportType;
    private LocalDate generatedOn;
    private String content;

    public Report(String reportType, String content) {
        this.reportType = reportType;
        this.content = content;
        this.generatedOn = LocalDate.now();
    }

    @Override
    public String generateReport() {
        return "=== " + reportType.toUpperCase() + " REPORT ===\n"
                + "Generated on: " + generatedOn + "\n\n"
                + content;
    }
}