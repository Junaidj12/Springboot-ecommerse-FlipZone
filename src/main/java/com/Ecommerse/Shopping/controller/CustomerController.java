package com.Ecommerse.Shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.Ecommerse.Shopping.entity.CartItem;
import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.repository.ProductRepository;
import com.Ecommerse.Shopping.service.AdminService;
import com.Ecommerse.Shopping.service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private ProductRepository productRepository;

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
	public String loadCustomerHome(HttpSession session, ModelMap model, 
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String sort,
			@RequestParam(defaultValue = "false") boolean desc,
			@RequestParam(defaultValue = "all") String stock,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size) {

		if (session.getAttribute("customer") == null) {
			session.setAttribute("fail", "Invalid session. Please login again");
			return "redirect:/login";
		}

		return customerService.loadProductsForCustomer(session, model, name, sort, desc, stock, page, size);
	}

	@GetMapping("/cart")
	public String cart(HttpSession session, ModelMap model) {
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) {
			session.setAttribute("fail", "Please login to view cart.");
			return "redirect:/login";
		}

		List<CartItem> cartItems = customerService.getCartItems(customer);
		model.addAttribute("cartItems", cartItems);

		long total = customerService.calculateTotal(cartItems);
		model.addAttribute("totalPrice", total);

		return "cart.html";
	}

	@PostMapping("/add-to-cart")
	public String addToCart(@RequestParam Long productId, HttpSession session) {
		return customerService.addToCart(productId, session);
	}

	@GetMapping("/cart/increase")
	public String increaseItem() {
		return "redirect:/customer/cart";
	}

	@GetMapping("/cart/decrease")
	public String decreaseItem() {
		return "redirect:/customer/cart";
	}
	@PostMapping("/cart/update")
	public String updateCart(@RequestParam Long productId, @RequestParam int change, HttpSession session) {
	    Customer customer = (Customer) session.getAttribute("customer");
	    if (customer == null) {
	        session.setAttribute("fail", "Please login.");
	        return "redirect:/login";
	    }

	    customerService.updateCartQuantity(customer, productId, change);
	    return "redirect:/customer/cart";
	}
	@PostMapping("/buy-now")
	public String buyNow(@RequestParam Long productId, HttpSession session) {
	    Customer customer = (Customer) session.getAttribute("customer");
	    if (customer == null) {
	        session.setAttribute("fail", "Please login.");
	        return "redirect:/login";
	    }

	    // You can pass this to a checkout page, or mock success
	    session.setAttribute("pass", "Purchased product with ID: " + productId);
	    return "redirect:/customer/cart";
	}


}
