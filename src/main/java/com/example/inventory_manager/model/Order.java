package com.example.inventory_manager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private List<InventoryItem> orderedItems;
    private Date orderDate;
    private String status;
    private Supplier supplier;

    public Order(int orderId, Supplier supplier) {
        this.orderId = orderId;
        this.supplier = supplier;
        this.orderedItems = new ArrayList<>();
        this.orderDate = new Date();
        this.status = "Pending";
    }

    public void addItem(InventoryItem item) {
        if (item.getCurrentStock() <= 0)
            throw new IllegalArgumentException("Cannot order item with zero stock.");
        orderedItems.add(item);
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (InventoryItem item : orderedItems) {
            total += item.getProduct().getPrice() * item.getCurrentStock();
        }
        return total;
    }

    public void markAsCompleted() {
        this.status = "Completed";
    }

    public String getStatus() {
        return status;
    }


    public int getOrderId() {
        return orderId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    // ===== END GETTERS =====

    @Override
    public String toString() {
        return "Order #" + orderId + " (" + status + ") - Items: " + orderedItems.size();
    }
}
