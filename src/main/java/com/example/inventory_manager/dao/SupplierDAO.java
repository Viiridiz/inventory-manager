package com.example.inventory_manager.dao;

import com.example.inventory_manager.model.Supplier;
import java.util.List;

public interface SupplierDAO {
    Supplier findById(int supplierId);
    List<Supplier> findAll();
    boolean save(Supplier supplier);
    boolean delete(int supplierId);
}
