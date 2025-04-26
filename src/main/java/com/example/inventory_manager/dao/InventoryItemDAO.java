package com.example.inventory_manager.dao;

import com.example.inventory_manager.model.InventoryItem;
import java.util.List;

/**
 * Interface for performing CRUD operations on InventoryItem entities.
 */
public interface InventoryItemDAO {

    /**
     * Finds an inventory item by its ID.
     *
     * @param inventoryId the ID of the inventory item
     * @return the InventoryItem if found, or null if not found
     */
    InventoryItem findById(int inventoryId);

    /**
     * Finds an inventory item by the associated product ID.
     *
     * @param productId the ID of the product
     * @return the InventoryItem if found, or null if not found
     */
    InventoryItem findByProductId(int productId);

    /**
     * Retrieves all inventory items from the database.
     *
     * @return a list of all inventory items
     */
    List<InventoryItem> findAll();

    /**
     * Saves an inventory item to the database.
     * Inserts if new, updates if existing.
     *
     * @param inventoryItem the inventory item to save
     * @return true if the operation was successful, false otherwise
     */
    boolean save(InventoryItem inventoryItem);

    /**
     * Deletes an inventory item from the database based on its ID.
     *
     * @param inventoryId the ID of the inventory item to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(int inventoryId);
}
