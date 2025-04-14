package com.example.inventory_manager.model;

public class Main {
    public static void main(String[] args) {
        Product p1 = new Product(1, "Mouse", "SKU123", "Electronics", 25.99, "Wireless Mouse");
        InventoryItem item1 = new InventoryItem(p1, 100, "A1");

        Supplier s1 = new Supplier(1, "Tech Supply Co.", "info@techsupply.com", "123-456-7890");
        s1.addProduct(p1);

        Order order = new Order(1001, s1);
        order.addItem(item1);

        Report report = new Report("Stock", item1.toString());

        System.out.println(report.generateReport());
    }
}
