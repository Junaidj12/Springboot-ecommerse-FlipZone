package com.Ecommerse.Shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.repository.ProductRepository;
import com.Ecommerse.Shopping.service.AdminService;
import com.Ecommerse.Shopping.service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@Autowired
	AdminService adminService;

	@Autowired
	ProductRepository productRepository;

	@GetMapping("/register")
	public String loadRegister() {
		return "register.html";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute Customer customer, HttpSession session) {
		return customerService.register(customer, session);
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
	public String loadCustomerHome(
	        HttpSession session,
	        ModelMap model,
	        @RequestParam(defaultValue = "") String name,
	        @RequestParam(defaultValue = "price") String sort,
	        @RequestParam(defaultValue = "false") boolean desc,
	        @RequestParam(defaultValue = "all") String stock,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "6") int size
	) {
	    if (session.getAttribute("customer") == null) {
	        session.setAttribute("fail", "Invalid session. Please login again");
	        return "redirect:/login";
	    }

	    return customerService.loadProductsForCustomer(session, model, name, sort, desc, stock, page, size);
	}


	@GetMapping("/cart")
	public String cart() {
		return "cart.html";
	}

	@GetMapping("/add-to-cart")
	public String addtocart() {
		return "redirect:/customer/view-products";
	}

	@GetMapping("/cart/increase")
	public String increaseItem() {
		return "redirect:/customer/cart";
	}

	@GetMapping("/cart/decrease")
	public String decreaseItem() {
		return "redirect:/customer/cart";
	}
}
