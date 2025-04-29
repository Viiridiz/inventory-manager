package com.example.inventory_manager.controller;

import com.example.inventory_manager.dao.impl.ProductDAOImpl;
import com.example.inventory_manager.dao.impl.SupplierDAOImpl;
import com.example.inventory_manager.dao.impl.InventoryItemDAOImpl;
import com.example.inventory_manager.db.SchemaInitializer;
import com.example.inventory_manager.model.InventoryReport;
import com.example.inventory_manager.model.Product;
import com.example.inventory_manager.model.Supplier;
import com.example.inventory_manager.model.InventoryItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet controller for managing the inventory system.
 * Handles product management, supplier management, stock updates, and report generation.
 */

@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {

    private ProductDAOImpl productDAO;
    private SupplierDAOImpl supplierDAO;
    private InventoryItemDAOImpl inventoryItemDAO;

    /**
     * Initializes database schema and inserts sample data if necessary.
     */

    @Override
    public void init() throws ServletException {
        SchemaInitializer.initialize();
        productDAO = new ProductDAOImpl();
        supplierDAO = new SupplierDAOImpl();
        inventoryItemDAO = new InventoryItemDAOImpl();

        try {
            // Insert sample supplier if none exists
            if (supplierDAO.findAll().isEmpty()) {
                Supplier defaultSupplier = new Supplier(0, "Default Supplier", "default@supplier.com", "123-456-7890");
                supplierDAO.save(defaultSupplier);
            }

            // Insert sample product if none exists
            if (productDAO.findAll().isEmpty()) {
                Product defaultProduct = new Product(0, "Sample Product", "SP001", "General", 10.99, "Demo product.");
                productDAO.save(defaultProduct);

                // Also insert an inventory item for it
                Product savedProduct = productDAO.findBySku("SP001");
                if (savedProduct != null) {
                    InventoryItem item = new InventoryItem(savedProduct, 10, "Warehouse A");
                    inventoryItemDAO.save(item);
                }
            }
        } catch (Exception e) {
            System.err.println("Error inserting sample data: " + e.getMessage());
        }
    }

    /**
     * Handles GET requests by loading products, suppliers, and inventory items from the database.
     * Forwards the data to the inventory.jsp page.
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Product> products = productDAO.findAll();
        List<Supplier> suppliers = supplierDAO.findAll();
        List<InventoryItem> inventoryItems = inventoryItemDAO.findAll();

        System.out.println("[DEBUG] Products loaded: " + products.size());
        System.out.println("[DEBUG] Suppliers loaded: " + suppliers.size());
        System.out.println("[DEBUG] InventoryItems loaded: " + inventoryItems.size());

        request.setAttribute("products", products);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("inventoryItems", inventoryItems);

        request.getRequestDispatcher("/inventory.jsp").forward(request, response);
    }

    /**
     * Handles POST requests for adding products, suppliers, updating stock, deleting items, and generating reports.
     */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("addProduct".equals(action)) {
            addProduct(request);
            request.getSession().setAttribute("flashMessage", "✅ New product added successfully!");
        } else if ("addSupplier".equals(action)) {
            addSupplier(request);
            request.getSession().setAttribute("flashMessage", "✅ New supplier added successfully!");
        } else if ("updateStock".equals(action)) {
            updateStock(request);
            request.getSession().setAttribute("flashMessage", "✅ Stock updated successfully!");
        } else if ("deleteProduct".equals(action)) {
            deleteProduct(request);
            request.getSession().setAttribute("flashMessage", "🗑️ Product deleted successfully!");
        } else if ("deleteSupplier".equals(action)) {
            deleteSupplier(request);
            request.getSession().setAttribute("flashMessage", "🗑️ Supplier deleted successfully!");
        } else if ("generateReport".equals(action)) {
            generateReport(request, response);
            request.getSession().setAttribute("flashMessage", "📄 Inventory report generated!");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/inventory");
    }

    /**
     * Generates and downloads the inventory report as a text file.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws IOException if an I/O error occurs
     */

    private void generateReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<InventoryItem> inventoryItems = inventoryItemDAO.findAll();
        InventoryReport report = new InventoryReport(inventoryItems);
        String reportText = report.generateReport();

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=inventory_report.txt");
        response.getWriter().write(reportText);
    }

    /**
     * Deletes a supplier by supplier ID.
     *
     * @param request HTTP request
     */

    private void deleteSupplier(HttpServletRequest request) {
        String supplierIdStr = request.getParameter("supplierId");

        if (supplierIdStr != null) {
            try {
                int supplierId = Integer.parseInt(supplierIdStr);
                supplierDAO.deleteById(supplierId);

                // Flash a success message
                request.getSession().setAttribute("flashMessage", "Supplier deleted successfully!");

            } catch (NumberFormatException e) {
                System.err.println("Invalid supplier ID for delete: " + e.getMessage());
            }
        }
    }

    /**
     * Deletes a product by product ID.
     * Also deletes the associated inventory item.
     *
     * @param request HTTP request
     */

    private void deleteProduct(HttpServletRequest request) {
        String productIdStr = request.getParameter("productId");

        if (productIdStr != null) {
            try {
                int productId = Integer.parseInt(productIdStr);

                // Delete inventory item first if it exists
                InventoryItem item = inventoryItemDAO.findByProductId(productId);
                if (item != null) {
                    inventoryItemDAO.deleteByProductId(productId);
                }

                // Delete the product
                productDAO.deleteById(productId);

                request.getSession().setAttribute("flashMessage", "Product deleted successfully!");

            } catch (NumberFormatException e) {
                System.err.println("Invalid product ID for delete: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a new product based on form data.
     *
     * @param request HTTP request
     */

    private void addProduct(HttpServletRequest request) {
        String name = request.getParameter("productName");
        String sku = request.getParameter("productSku");
        String category = request.getParameter("productCategory");
        String priceStr = request.getParameter("productPrice");
        String description = request.getParameter("productDescription");
        String quantityStr = request.getParameter("productQuantity");

        if (name != null && sku != null && priceStr != null && quantityStr != null) {
            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                Product product = new Product(0, name, sku, category, price, description);

                boolean saved = productDAO.save(product);

                if (saved) {
                    Product savedProduct = productDAO.findBySku(sku);
                    if (savedProduct != null) {
                        InventoryItem item = new InventoryItem(savedProduct, quantity, "Default Location");

                        inventoryItemDAO.save(item);
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid price or quantity input: " + e.getMessage());
            }
        }
    }


    /**
     * Adds a new supplier based on form data.
     *
     * @param request HTTP request
     */

    private void addSupplier(HttpServletRequest request) {
        String name = request.getParameter("supplierName");
        String email = request.getParameter("supplierEmail");
        String phone = request.getParameter("supplierPhone");

        if (name != null && email != null && phone != null) {
            Supplier supplier = new Supplier(0, name, email, phone);
            supplierDAO.save(supplier);
        }
    }

    /**
     * Updates stock quantity for a given product SKU.
     *
     * @param request HTTP request
     */

    private void updateStock(HttpServletRequest request) {
        String sku = request.getParameter("productSku");
        String quantityStr = request.getParameter("quantityChange");

        if (sku != null && quantityStr != null) {
            try {
                int quantityChange = Integer.parseInt(quantityStr);

                Product product = productDAO.findBySku(sku);

                if (product != null) {
                    // Load or create InventoryItem
                    InventoryItem item = inventoryItemDAO.findByProductId(product.getId());
                    if (item == null) {
                        item = new InventoryItem(product, 0, "Default Location");
                    }
                    item.setCurrentStock(item.getCurrentStock() + quantityChange);

                    inventoryItemDAO.save(item);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid quantity input: " + e.getMessage());
            }
        }
    }
}
