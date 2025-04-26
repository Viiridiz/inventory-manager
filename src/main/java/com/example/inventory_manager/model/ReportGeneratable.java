package com.example.inventory_manager.model;

/**
 * Interface for classes that can generate reports.
 * Classes implementing this interface must define how the report is generated.
 */
public interface ReportGeneratable {

    /**
     * Generates a textual representation of the report.
     *
     * @return the report content as a String
     */
    String generateReport();
}
