package com.Ecommerse.Shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerse.Shopping.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	
	 Optional<Customer> findByEmail(String email);
	    boolean existsByEmail(String email);
	    boolean existsByMobile(Long mobile);

}
