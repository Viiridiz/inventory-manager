package com.example.inventory_manager.dao.impl;

import com.example.inventory_manager.dao.OrderDAO;
import com.example.inventory_manager.db.DbUtil;
import com.example.inventory_manager.model.Order;
import com.example.inventory_manager.model.Supplier;

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

    @Override
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
                String status = rs.getString("status");

                Supplier dummySupplier = new Supplier(supplierId, "", "", "");
                Order order = new Order(orderId, dummySupplier);
                if ("Completed".equalsIgnoreCase(status)) {
                    order.markAsCompleted();
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

    @Override
    public boolean save(Order order) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // Check if order exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE order_id = ?");
            checkStmt.setInt(1, order.getOrderId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count > 0) {
                // Update
                stmt = conn.prepareStatement("UPDATE orders SET supplier_id=?, status=? WHERE order_id=?");
                stmt.setInt(1, order.getSupplier().getSupplierId());
                stmt.setString(2, order.getStatus());
                stmt.setInt(3, order.getOrderId());
            } else {
                // Insert
                stmt = conn.prepareStatement("INSERT INTO orders (supplier_id, order_date, status) VALUES (?, ?, ?)");
                stmt.setInt(1, order.getSupplier().getSupplierId());
                stmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
                stmt.setString(3, order.getStatus());
            }

            int result = stmt.executeUpdate();
            return result > 0;
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
