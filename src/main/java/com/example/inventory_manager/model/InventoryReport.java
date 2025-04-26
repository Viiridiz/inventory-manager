package com.example.inventory_manager.model;

import java.util.List;

public class InventoryReport implements ReportGeneratable {

    private List<InventoryItem> inventoryItems;

    public InventoryReport(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

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
