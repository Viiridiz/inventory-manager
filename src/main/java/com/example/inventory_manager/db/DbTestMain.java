package com.example.inventory_manager.db;

import java.sql.Connection;

public class DbTestMain {
    public static void main(String[] args) {
        System.out.println("==== Database Test Start ====");

        // Step 1: Try to connect
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            System.out.println("Connection successful!");

            // Step 2: Initialize Schema (Create tables if not exist)
            SchemaInitializer.initialize();
            System.out.println("Schema initialized successfully!");

        } catch (Exception e) {
            System.err.println("Error during DB connection or schema initialization: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }

        System.out.println("==== Database Test Complete ====");
    }
}
