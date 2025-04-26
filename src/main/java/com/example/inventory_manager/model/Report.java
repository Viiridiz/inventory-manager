package com.example.inventory_manager.model;

import java.time.LocalDate;

/**
 * Represents a generic report that can be generated within the inventory management system.
 * Implements the {@link ReportGeneratable} interface to enforce report generation behavior.
 */
public class Report implements ReportGeneratable {

    private String reportType;
    private LocalDate generatedOn;
    private String content;

    /**
     * Constructs a new Report with the specified type and content.
     * Automatically sets the generation date to the current date.
     *
     * @param reportType the type of the report (e.g., "Inventory", "Order")
     * @param content the content/body of the report
     */
    public Report(String reportType, String content) {
        this.reportType = reportType;
        this.content = content;
        this.generatedOn = LocalDate.now();
    }

    /**
     * Generates a formatted report string containing the type, generation date, and content.
     *
     * @return the formatted report as a String
     */
    @Override
    public String generateReport() {
        return "=== " + reportType.toUpperCase() + " REPORT ===\n"
                + "Generated on: " + generatedOn + "\n\n"
                + content;
    }
}
