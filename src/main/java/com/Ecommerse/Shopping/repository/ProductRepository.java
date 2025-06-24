package com.Ecommerse.Shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerse.Shopping.entity.product;


public interface ProductRepository extends JpaRepository<product, Long>{

}
