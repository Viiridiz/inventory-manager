package com.example.inventory_manager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the inventory system including products, suppliers, and inventory items.
 * Provides operations to add new products, suppliers, update stock levels, and generate unique IDs.
 */
public class InventoryManager {
    private List<Product> products;
    private List<Supplier> suppliers;
    private List<InventoryItem> inventoryItems;
    private int productIdCounter = 1;
    private int supplierIdCounter = 1;

    /**
     * Constructs a new InventoryManager with empty lists of products, suppliers, and inventory items.
     */
    public InventoryManager() {
        this.products = new ArrayList<>();
        this.suppliers = new ArrayList<>();
        this.inventoryItems = new ArrayList<>();
    }

    // ===== PRODUCT =====

    /**
     * Adds a new product to the inventory and initializes its stock to zero.
     *
     * @param product the product to add
     */
    public void addProduct(Product product) {
        products.add(product);
        inventoryItems.add(new InventoryItem(product, 0, "Unknown")); // start with 0 stock
    }

    /**
     * Returns the list of all products.
     *
     * @return list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Finds a product by its SKU (case-insensitive).
     *
     * @param sku the SKU of the product
     * @return the matching product, or null if not found
     */
    public Product findProductBySku(String sku) {
        for (Product p : products) {
            if (p.getSku().equalsIgnoreCase(sku)) {
                return p;
            }
        }
        return null;
    }

    // ===== SUPPLIER =====

    /**
     * Adds a new supplier to the system.
     *
     * @param supplier the supplier to add
     */
    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    /**
     * Returns the list of all suppliers.
     *
     * @return list of suppliers
     */
    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    // ===== STOCK =====

    /**
     * Updates the stock for a product by its SKU.
     *
     * @param sku            the SKU of the product
     * @param quantityChange the quantity change (positive or negative)
     * @throws IllegalArgumentException if the product with the given SKU is not found
     */
    public void updateStockForProduct(String sku, int quantityChange) {
        for (InventoryItem item : inventoryItems) {
            if (item.getProduct().getSku().equalsIgnoreCase(sku)) {
                item.updateStock(quantityChange);
                return;
            }
        }
        throw new IllegalArgumentException("Product with SKU " + sku + " not found.");
    }

    /**
     * Returns the list of all inventory items.
     *
     * @return list of inventory items
     */
    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    // ===== ID GENERATORS =====

    /**
     * Generates a unique ID for a new product.
     *
     * @return the next product ID
     */
    public int generateProductId() {
        return productIdCounter++;
    }

    /**
     * Generates a unique ID for a new supplier.
     *
     * @return the next supplier ID
     */
    public int generateSupplierId() {
        return supplierIdCounter++;
    }
}
