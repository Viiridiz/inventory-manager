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

/**
 * Servlet controller for managing orders.
 * Handles placing new orders, marking orders as completed, and updating inventory stock.
 */

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private OrderDAOImpl orderDAO;
    private SupplierDAOImpl supplierDAO;
    private ProductDAOImpl productDAO;
    private InventoryItemDAOImpl inventoryItemDAO;

    /**
     * Initializes DAO objects for order, supplier, product, and inventory management.
     */

    @Override
    public void init() {
        orderDAO = new OrderDAOImpl();
        supplierDAO = new SupplierDAOImpl();
        productDAO = new ProductDAOImpl();
        inventoryItemDAO = new InventoryItemDAOImpl();
    }

    /**
     * Handles GET requests by loading all orders, suppliers, and products.
     * Forwards the data to orders.jsp.
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Order> orders = orderDAO.findAll();
        request.setAttribute("orders", orders);

        List<Supplier> suppliers = supplierDAO.findAll();
        request.setAttribute("suppliers", suppliers);

        List<Product> products = productDAO.findAll();
        request.setAttribute("products", products);

        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to place a new order or complete an existing one.
     */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("completeOrder".equals(action)) {
            completeOrder(request, response);
        } else {
            placeOrder(request, response);
        }
    }

    /**
     * Places a new order based on the supplier ID, product SKU, and quantity provided by the user.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws IOException if an error occurs during redirection
     */

    private void placeOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String productSku = request.getParameter("productSku");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Supplier supplier = supplierDAO.findAll().stream()
                    .filter(s -> s.getSupplierId() == supplierId)
                    .findFirst()
                    .orElse(null);

            if (supplier == null) {
                throw new IllegalArgumentException("Supplier not found");
            }

            Product product = productDAO.findBySku(productSku);

            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }

            InventoryItem item = new InventoryItem(product, quantity, "Ordering");

            Order order = new Order(0, supplier);
            order.addItem(item);

            orderDAO.save(order);

            request.getSession().setAttribute("flashMessage", "✅ New order placed successfully!");


            response.sendRedirect(request.getContextPath() + "/order");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/order");
        }
    }

    /**
     * Marks an order as completed, updates its status, and increases inventory stock based on ordered items.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws IOException if an error occurs during redirection
     */

    private void completeOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            Order order = orderDAO.findById(orderId);

            if (order != null && "Pending".equals(order.getStatus())) {
                order.markAsCompleted();
                orderDAO.update(order);

                for (InventoryItem item : order.getOrderedItems()) {
                    InventoryItem existingItem = inventoryItemDAO.findByProductId(item.getProduct().getId());
                    if (existingItem != null) {
                        existingItem.updateStock(item.getCurrentStock());
                        inventoryItemDAO.save(existingItem);
                    } else {
                        inventoryItemDAO.save(new InventoryItem(
                                item.getProduct(),
                                item.getCurrentStock(),
                                "Default Location"
                        ));
                    }
                }

                request.getSession().setAttribute("flashMessage", "✅ Order #" + order.getOrderId() + " marked as completed!");
            }

            response.sendRedirect(request.getContextPath() + "/order");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/order");
        }
    }

}
