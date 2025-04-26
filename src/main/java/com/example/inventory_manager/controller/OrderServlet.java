package com.example.inventory_manager.controller;

import com.example.inventory_manager.dao.impl.OrderDAOImpl;
import com.example.inventory_manager.dao.impl.SupplierDAOImpl;
import com.example.inventory_manager.dao.impl.ProductDAOImpl;
import com.example.inventory_manager.dao.impl.InventoryItemDAOImpl;
import com.example.inventory_manager.model.Order;
import com.example.inventory_manager.model.Supplier;
import com.example.inventory_manager.model.Product;
import com.example.inventory_manager.model.InventoryItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private OrderDAOImpl orderDAO;
    private SupplierDAOImpl supplierDAO;
    private ProductDAOImpl productDAO;
    private InventoryItemDAOImpl inventoryItemDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAOImpl();
        supplierDAO = new SupplierDAOImpl();
        productDAO = new ProductDAOImpl();
        inventoryItemDAO = new InventoryItemDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Load all orders
        List<Order> orders = orderDAO.findAll();
        request.setAttribute("orders", orders);

        // Load all suppliers
        List<Supplier> suppliers = supplierDAO.findAll();
        request.setAttribute("suppliers", suppliers);

        // Load all products ✅ (missing before)
        List<Product> products = productDAO.findAll();
        request.setAttribute("products", products);

        // Forward to JSP
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get form data
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String productSku = request.getParameter("productSku");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Fetch Supplier
            Supplier supplier = supplierDAO.findAll().stream()
                    .filter(s -> s.getSupplierId() == supplierId)
                    .findFirst()
                    .orElse(null);

            if (supplier == null) {
                throw new IllegalArgumentException("Supplier not found");
            }

            // Fetch Product
            Product product = productDAO.findBySku(productSku);

            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }

            // Create InventoryItem (local only for order)
            InventoryItem item = new InventoryItem(product, quantity, "Ordering");

            // Create Order
            Order order = new Order(0, supplier);
            order.addItem(item);

            // Save order
            orderDAO.save(order);

            System.out.println("✅ Order placed successfully!");

            // Redirect back to order page
            response.sendRedirect(request.getContextPath() + "/order");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/order");
        }
    }
}
