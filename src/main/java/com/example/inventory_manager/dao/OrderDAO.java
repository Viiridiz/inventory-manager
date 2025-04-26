package com.example.inventory_manager.dao;

import com.example.inventory_manager.model.Order;
import java.util.List;

public interface OrderDAO {
    Order findById(int orderId);
    List<Order> findAll();
    boolean save(Order order);
    boolean delete(int orderId);
}
