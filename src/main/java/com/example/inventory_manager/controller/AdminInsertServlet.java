package com.example.inventory_manager.controller;

import com.example.inventory_manager.dao.impl.ProductDAOImpl;
import com.example.inventory_manager.dao.impl.SupplierDAOImpl;
import com.example.inventory_manager.model.Product;
import com.example.inventory_manager.model.Supplier;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/insert")
public class AdminInsertServlet extends HttpServlet {

    private ProductDAOImpl productDAO;
    private SupplierDAOImpl supplierDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        supplierDAO = new SupplierDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Insert Dummy Supplier
        Supplier supplier = new Supplier(0, "Default Supplier", "default@supplier.com", "123-456-7890");
        supplierDAO.save(supplier);

        // Insert Dummy Product
        Product product = new Product(0, "Wireless Mouse", "MOUSE123", "Electronics", 29.99, "Basic wireless mouse");
        productDAO.save(product);

        response.setContentType("text/html");
        response.getWriter().println("<h1>✅ Dummy Supplier and Product inserted successfully!</h1>");
        response.getWriter().println("<a href='" + request.getContextPath() + "/order'>Go to Orders</a>");
    }
}
