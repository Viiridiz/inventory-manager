package com.example.inventory_manager.model;

/**
 * Represents an item stored in inventory.
 * Contains a reference to a product, its quantity in stock, and the location where it's stored.
 * Implements the StockTrackable interface to support stock management operations.
 */

public class InventoryItem implements StockTrackable {
    private Product product;
    private int quantity;
    private String location;

    /**
     * Constructs a new InventoryItem with the given product, quantity, and location.
     *
     * @param product  the product associated with this inventory item
     * @param quantity the quantity in stock
     * @param location the location of the stock
     */

    public InventoryItem(Product product, int quantity, String location) {
        this.product = product;
        this.quantity = quantity;
        this.location = location;
    }


    /**
     * Updates the current stock based on a quantity change.
     *
     * @param quantityChange the amount to change the stock (positive to increase, negative to decrease)
     * @throws RuntimeException if stock would fall below zero
     */

    @Override
    public void updateStock(int quantityChange) {
        quantity += quantityChange;
        if (quantity < 0) throw new RuntimeException("Stock cannot go below zero.");
    }

    /**
     * Returns the current stock quantity.
     *
     * @return the quantity in stock
     */

    @Override
    public int getCurrentStock() {
        return quantity;
    }


    /**
     * Sets the current stock quantity.
     *
     * @param currentStock the new stock quantity
     */

    public void setCurrentStock(int currentStock) {
        this.quantity = currentStock;
    }


    /**
     * Returns the product associated with this inventory item.
     *
     * @return the product
     */
    public Product getProduct() { return product; }

    /**
     * Returns the storage location of the inventory item.
     *
     * @return the location as a String
     */

    public String getLocation() { return location; }

    /**
     * Returns a string representation of the inventory item.
     *
     * @return a formatted string showing product name, stock quantity, and location
     */

    @Override
    public String toString() {
        return product.getName() + " - Stock: " + quantity + " @ " + location;
    }
}
