package com.example.inventory_manager.dao;

import com.example.inventory_manager.model.Order;
import java.util.List;

/**
 * Interface for performing CRUD operations on Order entities.
 */
public interface OrderDAO {

    /**
     * Finds an order by its ID.
     *
     * @param orderId the ID of the order
     * @return the Order if found, or null if not found
     */
    Order findById(int orderId);

    /**
     * Retrieves all orders from the database.
     *
     * @return a list of all orders
     */
    List<Order> findAll();

    /**
     * Saves an order to the database.
     * Inserts if new, updates if existing.
     *
     * @param order the order to save
     * @return true if the operation was successful, false otherwise
     */
    boolean save(Order order);

    /**
     * Deletes an order from the database based on its ID.
     *
     * @param orderId the ID of the order to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(int orderId);
}
