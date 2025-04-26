package com.example.inventory_manager.dao;

import com.example.inventory_manager.model.InventoryItem;
import java.util.List;

public interface InventoryItemDAO {
    InventoryItem findById(int inventoryId);
    InventoryItem findByProductId(int productId);
    List<InventoryItem> findAll();
    boolean save(InventoryItem inventoryItem);
    boolean delete(int inventoryId);
}
