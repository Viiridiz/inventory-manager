package com.example.inventory_manager.dao.impl;

import com.example.inventory_manager.dao.OrderDAO;
import com.example.inventory_manager.db.DbUtil;
import com.example.inventory_manager.model.Order;
import com.example.inventory_manager.model.Supplier;
import com.example.inventory_manager.model.InventoryItem;
import com.example.inventory_manager.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
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

                Supplier dummySupplier = new Supplier(supplierId, "", "", "");
                Order order = new Order(orderId, dummySupplier);
                if ("Completed".equalsIgnoreCase(status)) {
                    order.markAsCompleted();
                }
                return order;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error finding order: " + e.getMessage());
            return null;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    private List<InventoryItem> loadOrderedItemsForOrder(int orderId) {
        List<InventoryItem> items = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT p.*, oi.quantity FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?"
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


    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders");

            while (rs.next()) {
                int id = rs.getInt("order_id");
                int supplierId = rs.getInt("supplier_id");
                Date date = rs.getDate("order_date");
                String status = rs.getString("status");

                Supplier supplier = new SupplierDAOImpl().findById(supplierId);

                Order order = new Order(id, supplier);
                order.setOrderDate(date);
                order.setStatus(status);

                // NEW: Load ordered items for this order
                List<InventoryItem> items = loadOrderedItemsForOrder(id);
                for (InventoryItem item : items) {
                    order.addItem(item);
                }

                orders.add(order);
            }

        } catch (SQLException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return orders;
    }


    @Override
    public boolean save(Order order) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // Insert into orders table
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO orders (supplier_id, order_date, status) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setInt(1, order.getSupplier().getSupplierId());
            stmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setString(3, order.getStatus());

            int result = stmt.executeUpdate();
            if (result == 0) {
                return false;
            }

            // Get generated order_id
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);

                // Save order items
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


    @Override
    public boolean delete(int orderId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM orders WHERE order_id = ?");
            stmt.setInt(1, orderId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
