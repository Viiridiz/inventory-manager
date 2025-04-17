package com.example.inventory_manager.model;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Product> products;
    private List<Supplier> suppliers;
    private List<InventoryItem> inventoryItems;
    private int productIdCounter = 1;
    private int supplierIdCounter = 1;

    public InventoryManager() {
        this.products = new ArrayList<>();
        this.suppliers = new ArrayList<>();
        this.inventoryItems = new ArrayList<>();
    }

    // ===== PRODUCT =====
    public void addProduct(Product product) {
        products.add(product);
        inventoryItems.add(new InventoryItem(product, 0, "Unknown")); // start with 0 stock
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product findProductBySku(String sku) {
        for (Product p : products) {
            if (p.getSku().equalsIgnoreCase(sku)) {
                return p;
            }
        }
        return null;
    }

    // ===== SUPPLIER =====
    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    // ===== STOCK =====
    public void updateStockForProduct(String sku, int quantityChange) {
        for (InventoryItem item : inventoryItems) {
            if (item.getProduct().getSku().equalsIgnoreCase(sku)) {
                item.updateStock(quantityChange);
                return;
            }
        }
        throw new IllegalArgumentException("Product with SKU " + sku + " not found.");
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    // ===== ID GENERATORS =====
    public int generateProductId() {
        return productIdCounter++;
    }

    public int generateSupplierId() {
        return supplierIdCounter++;
    }
}
