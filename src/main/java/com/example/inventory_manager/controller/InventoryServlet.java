package com.example.inventory_manager.controller;

import com.example.inventory_manager.model.InventoryManager;
import com.example.inventory_manager.model.InventoryItem;
import com.example.inventory_manager.model.Product;
import com.example.inventory_manager.model.Supplier;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Try to get the inventory manager
        InventoryManager manager = (InventoryManager) session.getAttribute("inventoryManager");

        if (manager == null) {
            // First time user session
            manager = new InventoryManager();

            // Optional: add some sample products/suppliers for first-time testing
            Product sampleProduct = new Product(1, "Keyboard", "KB123", "Electronics", 50.0, "Mechanical Keyboard");
            Supplier sampleSupplier = new Supplier(1, "Tech Supplies Inc.", "tech@supplies.com", "123-456-7890");
            sampleSupplier.addProduct(sampleProduct);

            manager.addProduct(sampleProduct);
            manager.addSupplier(sampleSupplier);

            session.setAttribute("inventoryManager", manager);
        }

        // Make manager available to the JSP page
        request.setAttribute("inventoryManager", manager);

        // Forward to inventory.jsp
        request.getRequestDispatcher("/inventory.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        InventoryManager manager = (InventoryManager) session.getAttribute("inventoryManager");

        if (manager == null) {
            manager = new InventoryManager();
            session.setAttribute("inventoryManager", manager);
        }

        String action = request.getParameter("action");

        if ("addProduct".equals(action)) {
            addProduct(request, manager);
        } else if ("addSupplier".equals(action)) {
            addSupplier(request, manager);
        } else if ("updateStock".equals(action)) {
            updateStock(request, manager);
        }

        // After handling POST, redirect back to /inventory
        response.sendRedirect(request.getContextPath() + "/inventory");
    }

    private void addProduct(HttpServletRequest request, InventoryManager manager) {
        String name = request.getParameter("productName");
        String sku = request.getParameter("productSku");
        String category = request.getParameter("productCategory");
        String priceStr = request.getParameter("productPrice");
        String description = request.getParameter("productDescription");

        if (name != null && sku != null && priceStr != null) {
            try {
                double price = Double.parseDouble(priceStr);
                Product product = new Product(manager.generateProductId(), name, sku, category, price, description);
                manager.addProduct(product);
            } catch (NumberFormatException e) {
                // Handle invalid price input (later can add error messages)
            }
        }
    }

    private void addSupplier(HttpServletRequest request, InventoryManager manager) {
        String name = request.getParameter("supplierName");
        String email = request.getParameter("supplierEmail");
        String phone = request.getParameter("supplierPhone");

        if (name != null && email != null && phone != null) {
            Supplier supplier = new Supplier(manager.generateSupplierId(), name, email, phone);
            manager.addSupplier(supplier);
        }
    }

    private void updateStock(HttpServletRequest request, InventoryManager manager) {
        String sku = request.getParameter("productSku");
        String quantityStr = request.getParameter("quantityChange");

        if (sku != null && quantityStr != null) {
            try {
                int quantityChange = Integer.parseInt(quantityStr);
                manager.updateStockForProduct(sku, quantityChange);
            } catch (NumberFormatException e) {
                // Handle invalid quantity input
            }
        }
    }
}
