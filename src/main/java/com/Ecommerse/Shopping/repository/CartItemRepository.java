package com.Ecommerse.Shopping.repository;

import com.Ecommerse.Shopping.entity.CartItem;
import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.entity.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(Customer customer);

    // ✅ Add this if not present
    CartItem findByCustomerAndProduct(Customer customer, product product);
}