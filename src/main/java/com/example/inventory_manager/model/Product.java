package com.example.inventory_manager.model;

/**
 * Represents a product in the inventory system.
 * Each product has an ID, name, SKU, category, price, and description.
 */
public class Product {
    private int id;
    private String name;
    private String sku;
    private String category;
    private double price;
    private String description;

    /**
     * Constructs a new Product with the given attributes.
     *
     * @param id          the unique identifier for the product
     * @param name        the name of the product
     * @param sku         the SKU (Stock Keeping Unit) code
     * @param category    the product category
     * @param price       the price of the product
     * @param description the product description
     */
    public Product(int id, String name, String sku, String category, double price, String description) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.category = category;
        setPrice(price); // Validation
        this.description = description;
    }

    /**
     * Sets the product price.
     * Throws an exception if the price is negative.
     *
     * @param price the new price
     * @throws IllegalArgumentException if price is negative
     */
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    /**
     * Returns the product's price.
     *
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the product's name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the product's SKU code.
     *
     * @return SKU
     */
    public String getSku() {
        return sku;
    }

    /**
     * Returns the product's category.
     *
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the product's description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the product's unique ID.
     *
     * @return ID
     */
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + " (" + sku + ") - $" + price;
    }
}
