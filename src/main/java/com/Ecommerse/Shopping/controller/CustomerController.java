package com.Ecommerse.Shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/register")
	public String loadRegister() {
		return "register.html";
	}
	@PostMapping("/register")
	public String register(@ModelAttribute Customer customer, HttpSession session) {
		return customerService.register(customer,session);
	}
	@GetMapping("/otp")
	public String loadOtp() {
		return "otp.html";
	}
	@PostMapping("/submit-otp")
	public String submitOtp(@RequestParam int otp, HttpSession session) {
		return customerService.submitOtp(otp, session);
	}
	@GetMapping("/home")
	public String loadHome() {
		return "customer-home.html";
	}
	@GetMapping("/view-products")
	public String viewProducts() {
		return "products.html";
	}
	@GetMapping("/cart")
	public String cart() {
		return "cart.html";
	}
	@GetMapping("/add-to-cart")
	public String addtocart() {
		return "redirect:/customer/view-products";
	}
	
}
