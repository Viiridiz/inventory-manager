package com.example.inventory_manager.model;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private int supplierId;
    private String name;
    private String contactEmail;
    private String phone;
    private List<Product> suppliedProducts;

    public Supplier(int supplierId, String name, String contactEmail, String phone) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.suppliedProducts = new ArrayList<>();
    }

    public void addProduct(Product product) {
        suppliedProducts.add(product);
    }

    public List<Product> getSuppliedProducts() {
        return suppliedProducts;
    }

    public String getName() { return name; }
    public String getContactEmail() { return contactEmail; }
    public String getPhone() { return phone; }

    public int getSupplierId() {
        return supplierId;
    }
}
