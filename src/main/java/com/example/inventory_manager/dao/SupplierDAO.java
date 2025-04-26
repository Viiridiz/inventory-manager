package com.example.inventory_manager.dao;

import com.example.inventory_manager.model.Supplier;
import java.util.List;

/**
 * Interface for performing CRUD operations on Supplier entities.
 */
public interface SupplierDAO {

    /**
     * Finds a supplier by its ID.
     *
     * @param supplierId the ID of the supplier
     * @return the Supplier if found, or null if not found
     */
    Supplier findById(int supplierId);

    /**
     * Retrieves all suppliers from the database.
     *
     * @return a list of all suppliers
     */
    List<Supplier> findAll();

    /**
     * Saves a supplier to the database.
     * Inserts if new, updates if existing.
     *
     * @param supplier the supplier to save
     * @return true if the operation was successful, false otherwise
     */
    boolean save(Supplier supplier);

    /**
     * Deletes a supplier from the database based on its ID.
     *
     * @param supplierId the ID of the supplier to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(int supplierId);
}
