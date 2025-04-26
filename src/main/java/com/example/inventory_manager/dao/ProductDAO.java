package com.example.inventory_manager.dao;

import com.example.inventory_manager.model.Product;
import java.util.List;

/**
 * Interface for performing CRUD operations on Product entities.
 */
public interface ProductDAO {

    /**
     * Finds a product by its SKU (Stock Keeping Unit).
     *
     * @param sku the SKU of the product
     * @return the Product if found, or null if not found
     */
    Product findBySku(String sku);

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    List<Product> findAll();

    /**
     * Saves a product to the database.
     * Inserts if new, updates if existing.
     *
     * @param product the product to save
     * @return true if the operation was successful, false otherwise
     */
    boolean save(Product product);

    /**
     * Deletes a product from the database based on its SKU.
     *
     * @param sku the SKU of the product to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(String sku);
}
