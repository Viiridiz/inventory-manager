package com.example.inventory_manager.dao.impl;

import com.example.inventory_manager.db.DbUtil;
import com.example.inventory_manager.model.Order;
import com.example.inventory_manager.model.Supplier;
import com.example.inventory_manager.model.InventoryItem;
import com.example.inventory_manager.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for handling Order-related database operations.
 */

public class OrderDAOImpl {

    private SupplierDAOImpl supplierDAO = new SupplierDAOImpl();
    private ProductDAOImpl productDAO = new ProductDAOImpl();


    /**
     * Saves a new order and its ordered items into the database.
     *
     * @param order the Order to save
     * @return true if the order was successfully saved, false otherwise
     */

    public boolean save(Order order) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // Insert into orders table
            PreparedStatement orderStmt = conn.prepareStatement(
                    "INSERT INTO orders (supplier_id, order_date, status) VALUES (?, CURRENT_DATE, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            orderStmt.setInt(1, order.getSupplier().getSupplierId());
            orderStmt.setString(2, order.getStatus());

            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);

                // Save each order item
                for (InventoryItem item : order.getOrderedItems()) {
                    PreparedStatement itemStmt = conn.prepareStatement(
                            "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)"
                    );
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProduct().getId());
                    itemStmt.setInt(3, item.getCurrentStock());
                    itemStmt.executeUpdate();
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("Error saving order: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Retrieves all orders from the database, including their ordered items.
     *
     * @return a list of all Orders
     */

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders");

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int supplierId = rs.getInt("supplier_id");
                Date orderDate = rs.getDate("order_date");
                String status = rs.getString("status");

                Supplier supplier = supplierDAO.findById(supplierId);

                Order order = new Order(orderId, supplier);
                order.setStatus(status);
                order.setOrderDate(orderDate);

                // Load ordered items
                List<InventoryItem> items = loadOrderItems(orderId);
                for (InventoryItem item : items) {
                    order.addItem(item);
                }

                orders.add(order);
            }

        } catch (SQLException e) {
            System.err.println("Error finding all orders: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return orders;
    }

    /**
     * Finds a specific order by its ID.
     *
     * @param orderId the ID of the order to find
     * @return the found Order, or null if not found
     */

    public Order findById(int orderId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE order_id = ?");
            stmt.setInt(1, orderId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int supplierId = rs.getInt("supplier_id");
                Date orderDate = rs.getDate("order_date");
                String status = rs.getString("status");

                Supplier supplier = supplierDAO.findById(supplierId);

                Order order = new Order(orderId, supplier);
                order.setStatus(status);
                order.setOrderDate(orderDate);

                List<InventoryItem> items = loadOrderItems(orderId);
                for (InventoryItem item : items) {
                    order.addItem(item);
                }

                return order;
            }
        } catch (SQLException e) {
            System.err.println("Error finding order by ID: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return null;
    }

    /**
     * Updates the status of an existing order.
     *
     * @param order the Order object with updated information
     * @return true if the update was successful, false otherwise
     */

    public boolean update(Order order) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE orders SET status = ? WHERE order_id = ?"
            );
            stmt.setString(1, order.getStatus());
            stmt.setInt(2, order.getOrderId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Loads the ordered items associated with a specific order ID.
     *
     * @param orderId the ID of the order
     * @return a list of InventoryItems for the order
     */

    private List<InventoryItem> loadOrderItems(int orderId) {
        List<InventoryItem> items = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT oi.quantity, p.* FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?"
            );
            stmt.setInt(1, orderId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("id");
                String name = rs.getString("name");
                String sku = rs.getString("sku");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                int quantity = rs.getInt("quantity");

                Product product = new Product(productId, name, sku, category, price, description);
                InventoryItem item = new InventoryItem(product, quantity, "Ordering");

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error loading ordered items: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return items;
    }
}
