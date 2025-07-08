package com.Ecommerse.Shopping.repository;

import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.entity.Order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByCustomer(Customer customer);
	 Page<Order> findByCustomer(Customer customer, Pageable pageable);
	 
    
	 
	 @Query("SELECT COUNT(o) FROM Order o")
	    long countTotalOrders();

	    @Query("SELECT SUM(o.amount) FROM Order o")
	    Double getTotalSalesAmount();
}