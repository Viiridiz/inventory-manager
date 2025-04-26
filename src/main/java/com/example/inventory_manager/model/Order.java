package com.example.inventory_manager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an order placed with a supplier.
 * An order contains multiple inventory items, a status, and an associated supplier.
 */
public class Order {
    private int orderId;
    private List<InventoryItem> orderedItems;
    private Date orderDate;
    private String status;
    private Supplier supplier;

    /**
     * Constructs a new Order with the given ID and supplier.
     * The order starts with a "Pending" status and the current date.
     *
     * @param orderId  the unique identifier of the order
     * @param supplier the supplier associated with the order
     */
    public Order(int orderId, Supplier supplier) {
        this.orderId = orderId;
        this.supplier = supplier;
        this.orderedItems = new ArrayList<>();
        this.orderDate = new Date();
        this.status = "Pending";
    }

    /**
     * Adds an inventory item to the order.
     * Throws an exception if the item's stock is zero or less.
     *
     * @param item the inventory item to add
     * @throws IllegalArgumentException if the item's stock is zero or negative
     */
    public void addItem(InventoryItem item) {
        if (item.getCurrentStock() <= 0)
            throw new IllegalArgumentException("Cannot order item with zero stock.");
        orderedItems.add(item);
    }

    /**
     * Returns the list of ordered items.
     *
     * @return list of inventory items
     */
    public List<InventoryItem> getOrderedItems() {
        return orderedItems;
    }

    /**
     * Calculates the total price of the order.
     *
     * @return total price
     */
    public double calculateTotalPrice() {
        double total = 0;
        for (InventoryItem item : orderedItems) {
            total += item.getProduct().getPrice() * item.getCurrentStock();
        }
        return total;
    }

    /**
     * Marks the order status as "Completed".
     */
    public void markAsCompleted() {
        this.status = "Completed";
    }

    /**
     * Returns the current status of the order.
     *
     * @return order status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets a new status for the order.
     *
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the order ID.
     *
     * @return order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Returns the supplier associated with this order.
     *
     * @return supplier
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Returns the date when the order was created.
     *
     * @return order date
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * Sets a new order date.
     *
     * @param orderDate the new order date
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " (" + status + ") - Items: " + orderedItems.size();
    }
}
