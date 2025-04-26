package com.example.inventory_manager.dao.impl;

import com.example.inventory_manager.dao.SupplierDAO;
import com.example.inventory_manager.db.DbUtil;
import com.example.inventory_manager.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for handling Supplier-related database operations.
 */

public class SupplierDAOImpl implements SupplierDAO {

    /**
     * Finds a supplier in the database by its ID.
     *
     * @param supplierId the ID of the supplier
     * @return the matching Supplier or null if not found
     */

    @Override
    public Supplier findById(int supplierId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM suppliers WHERE supplier_id = ?");
            stmt.setInt(1, supplierId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("contact_email");
                String phone = rs.getString("phone");
                return new Supplier(supplierId, name, email, phone);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error finding supplier: " + e.getMessage());
            return null;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Deletes a supplier from the database by its ID.
     *
     * @param supplierId the ID of the supplier to delete
     * @return true if the deletion was successful, false otherwise
     */

    public boolean deleteById(int supplierId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM suppliers WHERE supplier_id = ?");
            stmt.setInt(1, supplierId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Retrieves all suppliers from the database.
     *
     * @return a list of all suppliers
     */

    @Override
    public List<Supplier> findAll() {
        List<Supplier> suppliers = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM suppliers");

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact_email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all suppliers: " + e.getMessage());
        } finally {
            DbUtil.closeQuietly(conn);
        }
        return suppliers;
    }

    /**
     * Saves a new supplier or updates an existing supplier in the database.
     *
     * @param supplier the Supplier to save or update
     * @return true if the save or update was successful, false otherwise
     */

    @Override
    public boolean save(Supplier supplier) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // Check if supplier exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM suppliers WHERE supplier_id = ?");
            checkStmt.setInt(1, supplier.getSupplierId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            PreparedStatement stmt;
            if (count > 0) {
                // Update
                stmt = conn.prepareStatement("UPDATE suppliers SET name=?, contact_email=?, phone=? WHERE supplier_id=?");
                stmt.setString(1, supplier.getName());
                stmt.setString(2, supplier.getContactEmail());
                stmt.setString(3, supplier.getPhone());
                stmt.setInt(4, supplier.getSupplierId());
            } else {
                // Insert
                stmt = conn.prepareStatement("INSERT INTO suppliers (name, contact_email, phone) VALUES (?, ?, ?)");
                stmt.setString(1, supplier.getName());
                stmt.setString(2, supplier.getContactEmail());
                stmt.setString(3, supplier.getPhone());
            }

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error saving supplier: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }

    /**
     * Deletes a supplier from the database by its ID.
     *
     * @param supplierId the ID of the supplier
     * @return true if deletion was successful, false otherwise
     */

    @Override
    public boolean delete(int supplierId) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM suppliers WHERE supplier_id = ?");
            stmt.setInt(1, supplierId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            return false;
        } finally {
            DbUtil.closeQuietly(conn);
        }
    }
}
