package com.example.inventory_manager.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {

    public static void initialize() {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();

            // Products table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS products (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                sku VARCHAR(100) UNIQUE NOT NULL,
                category VARCHAR(100),
                price DOUBLE,
                description VARCHAR(500)
            )
            """);

            // Suppliers table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS suppliers (
                supplier_id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                contact_email VARCHAR(255),
                phone VARCHAR(50)
            )
            """);

            // Orders table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS orders (
                order_id INT AUTO_INCREMENT PRIMARY KEY,
                supplier_id INT,
                order_date DATE,
                status VARCHAR(50),
                FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
            )
            """);

            // Inventory Items table
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS inventory_items (
                inventory_id INT AUTO_INCREMENT PRIMARY KEY,
                product_id INT,
                quantity INT,
                location VARCHAR(255),
                FOREIGN KEY (product_id) REFERENCES products(id)
            )
            """);

            // Order Items table (after orders/products exist!)
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS order_items (
                id INT AUTO_INCREMENT PRIMARY KEY,
                order_id INT,
                product_id INT,
                quantity INT,
                FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
            )
            """);

            System.out.println("Database schema initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
