package com.Ecommerse.Shopping.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerse.Shopping.entity.product;

public interface ProductRepository extends JpaRepository<product, Long> {

	List<product> findByNameContainingIgnoreCase(String name, Sort sort);

	List<product> findByNameContainingIgnoreCaseAndStockGreaterThan(String name, int stock, Sort sort);

	Page<product> findByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<product> findByNameContainingIgnoreCaseAndStockGreaterThan(String name, int stock, Pageable pageable);


}