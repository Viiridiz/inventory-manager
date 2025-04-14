package com.example.inventory_manager.model;

public class Product {
    private int id;
    private String name;
    private String sku;
    private String category;
    private double price;
    private String description;

    public Product(int id, String name, String sku, String category, double price, String description) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.category = category;
        setPrice(price); // validation
        this.name = name;
        this.description = description;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public double getPrice() { return price; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public int getId() { return id; }

    @Override
    public String toString() {
        return name + " (" + sku + ") - $" + price;
    }
}
