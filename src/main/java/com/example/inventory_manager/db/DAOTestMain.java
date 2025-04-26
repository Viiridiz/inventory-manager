package com.example.inventory_manager.db;

import com.example.inventory_manager.dao.impl.ProductDAOImpl;
import com.example.inventory_manager.dao.impl.SupplierDAOImpl;
import com.example.inventory_manager.dao.impl.InventoryItemDAOImpl;
import com.example.inventory_manager.dao.impl.OrderDAOImpl;
import com.example.inventory_manager.model.Product;
import com.example.inventory_manager.model.Supplier;
import com.example.inventory_manager.model.InventoryItem;
import com.example.inventory_manager.model.Order;

public class DAOTestMain {
    public static void main(String[] args) {
        System.out.println("==== DAO Test Start ====");

        SchemaInitializer.initialize();

        ProductDAOImpl productDAO = new ProductDAOImpl();
        SupplierDAOImpl supplierDAO = new SupplierDAOImpl();
        InventoryItemDAOImpl inventoryItemDAO = new InventoryItemDAOImpl();
        OrderDAOImpl orderDAO = new OrderDAOImpl();

        // Save Supplier
        Supplier supplier = new Supplier(0, "Test Supplier", "test@supplier.com", "123-456-7890");
        supplierDAO.save(supplier);
        System.out.println("✅ Supplier saved.");

        // Load Supplier back (latest inserted supplier)
        Supplier savedSupplier = supplierDAO.findAll().get(0);
        System.out.println("✅ Supplier reloaded with ID: " + savedSupplier.getSupplierId());

        // Save Product
        Product product = new Product(0, "Test Product", "SKU123", "Electronics", 49.99, "Test Description");
        productDAO.save(product);
        System.out.println("✅ Product saved.");

        // Load Product back
        Product savedProduct = productDAO.findBySku("SKU123");
        System.out.println("✅ Product reloaded with ID: " + savedProduct.getId());

        // Save InventoryItem with real Product ID
        InventoryItem inventoryItem = new InventoryItem(savedProduct, 100, "Warehouse A");
        inventoryItemDAO.save(inventoryItem);
        System.out.println("✅ Inventory item saved.");

        // Save Order with real Supplier ID
        Order order = new Order(0, savedSupplier);
        order.addItem(inventoryItem);
        orderDAO.save(order);
        System.out.println("✅ Order saved.");

        System.out.println("==== DAO Test Complete ====");
    }
}
