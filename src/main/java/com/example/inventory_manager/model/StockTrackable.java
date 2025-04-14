package com.example.inventory_manager.model;

public interface StockTrackable {
    void updateStock(int quantity);
    int getCurrentStock();
}
