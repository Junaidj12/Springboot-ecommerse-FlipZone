package com.Ecommerse.Shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerse.Shopping.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	
	boolean existsByEmail(String email);

	boolean existsByMobile(Long mobile);
	
	Customer findByEmail(String email);
}
