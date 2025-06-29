package com.Ecommerse.Shopping.service;

import com.Ecommerse.Shopping.entity.Order;
import com.Ecommerse.Shopping.entity.OrderStatus;
import com.Ecommerse.Shopping.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
    }

    

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    

}
