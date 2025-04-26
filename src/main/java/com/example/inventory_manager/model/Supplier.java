package com.example.inventory_manager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a supplier who provides products to the inventory system.
 * Each supplier can be associated with multiple products.
 */
public class Supplier {

    private int supplierId;
    private String name;
    private String contactEmail;
    private String phone;
    private List<Product> suppliedProducts;

    /**
     * Constructs a new Supplier with the given details.
     *
     * @param supplierId the unique ID assigned to the supplier
     * @param name the name of the supplier
     * @param contactEmail the contact email address of the supplier
     * @param phone the phone number of the supplier
     */
    public Supplier(int supplierId, String name, String contactEmail, String phone) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.suppliedProducts = new ArrayList<>();
    }

    /**
     * Associates a product with this supplier.
     *
     * @param product the product to add to the supplier's list
     */
    public void addProduct(Product product) {
        suppliedProducts.add(product);
    }

    /**
     * Returns the list of products supplied by this supplier.
     *
     * @return list of supplied products
     */
    public List<Product> getSuppliedProducts() {
        return suppliedProducts;
    }

    /**
     * Returns the supplier's name.
     *
     * @return name of the supplier
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the supplier's contact email.
     *
     * @return contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Returns the supplier's phone number.
     *
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the supplier's unique ID.
     *
     * @return supplier ID
     */
    public int getSupplierId() {
        return supplierId;
    }
}
