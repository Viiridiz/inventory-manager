package com.example.inventory_manager.dao.impl;

import com.example.inventory_manager.dao.InventoryItemDAO;
import com.example.inventory_manager.db.DbUtil;
import com.example.inventory_manager.model.InventoryItem;
import com.example.inventory_manager.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of InventoryItemDAO for managing inventory item records in the database.
 */

public class InventoryItemDAOImpl implements InventoryItemDAO {

    /**
     * Finds an inventory item by its inventory ID.
     *
     * @param inventoryId the ID of the inventory item
     * @return the found InventoryItem, or null if not found
     */

    @Override
    public InventoryItem findById(int inventoryId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM inventory_items WHERE inventory_id = ?");
            stmt.setInt(1, inventoryId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                String location = rs.getString("location");

                Product dummyProduct = new Product(productId, "", "", "", 0.0, "");
                return new InventoryItem(dummyProduct, quantity, location);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error finding inventory item: " + e.getMessage());
            return null;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Deletes an inventory item based on the associated product ID.
     *
     * @param productId the ID of the product linked to the inventory item
     * @return true if deletion was successful, false otherwise
     */

    public boolean deleteByProductId(int productId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM inventory_items WHERE product_id = ?");
            stmt.setInt(1, productId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting inventory item: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Finds an inventory item based on the product ID.
     *
     * @param productId the product ID
     * @return the found InventoryItem, or null if not found
     */

    @Override
    public InventoryItem findByProductId(int productId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM inventory_items WHERE product_id = ?");
            stmt.setInt(1, productId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                String location = rs.getString("location");

                Product dummyProduct = new Product(productId, "", "", "", 0.0, "");
                return new InventoryItem(dummyProduct, quantity, location);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error finding inventory item by product: " + e.getMessage());
            return null;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Retrieves all inventory items from the database.
     *
     * @return a list of all InventoryItems
     */

    @Override
    public List<InventoryItem> findAll() {
        List<InventoryItem> items = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DbUtil.getConnection();
            String sql = """
                SELECT ii.inventory_id, ii.quantity, ii.location,
                       p.id AS product_id, p.name, p.sku, p.category, p.price, p.description
                FROM inventory_items ii
                JOIN products p ON ii.product_id = p.id
                """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Create Product
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("sku"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("description")
                );

                // Create InventoryItem
                InventoryItem item = new InventoryItem(
                        product,
                        rs.getInt("quantity"),
                        rs.getString("location")
                );

                items.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Error finding all inventory items: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return items;
    }

    /**
     * Saves an inventory item to the database.
     * Updates the record if it exists, or inserts a new record if it does not.
     *
     * @param inventoryItem the inventory item to save
     * @return true if the operation was successful, false otherwise
     */

    @Override
    public boolean save(InventoryItem inventoryItem) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // Check if inventory item exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM inventory_items WHERE product_id = ?");
            checkStmt.setInt(1, inventoryItem.getProduct().getId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count > 0) {
                // Update existing inventory item
                stmt = conn.prepareStatement("UPDATE inventory_items SET quantity=?, location=? WHERE product_id=?");
                stmt.setInt(1, inventoryItem.getCurrentStock());
                stmt.setString(2, inventoryItem.getLocation());
                stmt.setInt(3, inventoryItem.getProduct().getId());
            } else {
                // Insert new inventory item
                stmt = conn.prepareStatement("INSERT INTO inventory_items (product_id, quantity, location) VALUES (?, ?, ?)");
                stmt.setInt(1, inventoryItem.getProduct().getId());
                stmt.setInt(2, inventoryItem.getCurrentStock());
                stmt.setString(3, inventoryItem.getLocation());
            }

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error saving inventory item: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Deletes an inventory item based on its inventory ID.
     *
     * @param inventoryId the ID of the inventory item to delete
     * @return true if deletion was successful, false otherwise
     */

    @Override
    public boolean delete(int inventoryId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM inventory_items WHERE inventory_id = ?");
            stmt.setInt(1, inventoryId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting inventory item: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
