package com.Ecommerse.Shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerse.Shopping.entity.CartItem;
import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.entity.product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(Customer customer);
    CartItem findByCustomerAndProduct(Customer customer, product product);
}

