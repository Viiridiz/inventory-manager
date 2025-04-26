package com.example.inventory_manager.dao.impl;

import com.example.inventory_manager.dao.ProductDAO;
import com.example.inventory_manager.db.DbUtil;
import com.example.inventory_manager.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product findBySku(String sku) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE sku = ?");
            stmt.setString(1, sku);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                return new Product(rs.getInt("id"), name, sku, category, price, description);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error finding product: " + e.getMessage());
            return null;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }


    public boolean deleteById(int id) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE id = ?");
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }


    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("sku"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all products: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return products;
    }

    @Override
    public boolean save(Product product) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // Check if product exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE sku = ?");
            checkStmt.setString(1, product.getSku());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count > 0) {
                // Update
                stmt = conn.prepareStatement("UPDATE products SET name=?, category=?, price=?, description=? WHERE sku=?");
                stmt.setString(1, product.getName());
                stmt.setString(2, product.getCategory());
                stmt.setDouble(3, product.getPrice());
                stmt.setString(4, product.getDescription());
                stmt.setString(5, product.getSku());
            } else {
                // Insert
                stmt = conn.prepareStatement("INSERT INTO products (name, sku, category, price, description) VALUES (?, ?, ?, ?, ?)");
                stmt.setString(1, product.getName());
                stmt.setString(2, product.getSku());
                stmt.setString(3, product.getCategory());
                stmt.setDouble(4, product.getPrice());
                stmt.setString(5, product.getDescription());
            }

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error saving product: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    @Override
    public boolean delete(String sku) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE sku = ?");
            stmt.setString(1, sku);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
