package com.example.inventory_manager.model;

/**
 * Interface for classes that support stock tracking operations.
 * Classes implementing this interface can update and retrieve stock quantities.
 */
public interface StockTrackable {

    /**
     * Updates the stock quantity based on the given change.
     * The change can be positive (increase stock) or negative (decrease stock).
     *
     * @param quantity the quantity to add or subtract from the current stock
     */
    void updateStock(int quantity);

    /**
     * Returns the current stock quantity.
     *
     * @return the current stock as an integer
     */
    int getCurrentStock();
}
