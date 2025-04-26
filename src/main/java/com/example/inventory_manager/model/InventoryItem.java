package com.example.inventory_manager.model;

public class InventoryItem implements StockTrackable {
    private Product product;
    private int quantity;
    private String location;

    public InventoryItem(Product product, int quantity, String location) {
        this.product = product;
        this.quantity = quantity;
        this.location = location;
    }

    @Override
    public void updateStock(int quantityChange) {
        quantity += quantityChange;
        if (quantity < 0) throw new RuntimeException("Stock cannot go below zero.");
    }

    @Override
    public int getCurrentStock() {
        return quantity;
    }

    public void setCurrentStock(int currentStock) {
        this.quantity = currentStock;
    }


    public Product getProduct() { return product; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return product.getName() + " - Stock: " + quantity + " @ " + location;
    }
}
