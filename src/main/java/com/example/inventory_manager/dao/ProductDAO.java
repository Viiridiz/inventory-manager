package com.example.inventory_manager.dao;

import com.example.inventory_manager.model.Product;
import java.util.List;

public interface ProductDAO {
    Product findBySku(String sku);
    List<Product> findAll();
    boolean save(Product product);
    boolean delete(String sku);
}
