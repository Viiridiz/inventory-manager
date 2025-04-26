package com.example.inventory_manager.model;

import java.util.List;

/**
 * Generates a report summarizing the inventory status.
 * Implements the ReportGeneratable interface to provide a text-based report.
 */
public class InventoryReport implements ReportGeneratable {

    private List<InventoryItem> inventoryItems;

    /**
     * Constructs a new InventoryReport for the given list of inventory items.
     *
     * @param inventoryItems the list of inventory items to include in the report
     */
    public InventoryReport(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    /**
     * Generates a formatted inventory report.
     *
     * @return the report as a String
     */
    @Override
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Inventory Report ===\n\n");

        for (InventoryItem item : inventoryItems) {
            sb.append("Product: ").append(item.getProduct().getName())
                    .append(" | SKU: ").append(item.getProduct().getSku())
                    .append(" | Stock: ").append(item.getCurrentStock())
                    .append(" | Location: ").append(item.getLocation())
                    .append("\n");
        }

        sb.append("\nTotal Items: ").append(inventoryItems.size());

        return sb.toString();
    }
}
