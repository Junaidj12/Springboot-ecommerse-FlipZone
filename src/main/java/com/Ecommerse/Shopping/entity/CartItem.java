package com.Ecommerse.Shopping.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private product product;

    @ManyToOne
    private Customer customer;

    private int quantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public product getProduct() {
		return product;
	}

	public void setProduct(product product) {
		this.product = product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    
}

