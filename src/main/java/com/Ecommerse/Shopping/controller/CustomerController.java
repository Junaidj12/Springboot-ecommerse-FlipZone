package com.Ecommerse.Shopping.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.Ecommerse.Shopping.entity.CartItem;
import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.entity.Order;
import com.Ecommerse.Shopping.entity.OrderStatus;
import com.Ecommerse.Shopping.entity.product;
import com.Ecommerse.Shopping.repository.OrderRepository;
import com.Ecommerse.Shopping.repository.ProductRepository;
import com.Ecommerse.Shopping.service.AdminService;
import com.Ecommerse.Shopping.service.CustomerService;
import com.Ecommerse.Shopping.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;

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
			@RequestParam(defaultValue = "12") int size) {

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

	    model.addAttribute("customer", customer); // ✅ REQUIRED for Razorpay prefill

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
	@PostMapping("/payment-success")
	@ResponseBody
	public ResponseEntity<String> handlePaymentSuccess(@RequestBody Map<String, String> data, HttpSession session) {
	    Customer customer = (Customer) session.getAttribute("customer");
	    if (customer == null) {
	        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Login required");
	    }

	    Long productId = Long.parseLong(data.get("productId"));
	    String paymentId = data.get("razorpayPaymentId");

	    customerService.createOrderForProduct(customer, productId, paymentId);
	    return ResponseEntity.ok("Order placed and cart updated.");
	}
	@GetMapping("/orders")
	public String viewOrders(HttpSession session, ModelMap model) {
	    Customer customer = (Customer) session.getAttribute("customer");

	    if (customer == null) {
	        session.setAttribute("fail", "Please login to view your orders.");
	        return "redirect:/login";
	    }

	    List<Order> orders = customerService.getOrdersForCustomer(customer);
	    model.addAttribute("orders", orders);
	    return "my-orders.html"; // view page
	}
	@GetMapping("/my-orders")
	public String viewMyOrders(Model model, Principal principal,
	                           @RequestParam(defaultValue = "0") int page,
	                           @RequestParam(defaultValue = "5") int size,
	                           @RequestParam(defaultValue = "orderDate") String sort,
	                           @RequestParam(defaultValue = "true") boolean desc) {

	    Customer customer = customerService.findByEmail(principal.getName());
	    Page<Order> orderPage = customerService.getOrdersWithPagination(customer, page, size, sort, desc);

	    model.addAttribute("orders", orderPage.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", orderPage.getTotalPages());
	    model.addAttribute("size", size);
	    model.addAttribute("sort", sort);
	    model.addAttribute("desc", desc);

	    return "customer-orders";
	}


	@GetMapping("/order-invoice/{orderId}")
	public String downloadInvoice(@PathVariable Long orderId, ModelMap model, HttpSession session) {
	    Customer customer = (Customer) session.getAttribute("customer");
	    if (customer == null) {
	        session.setAttribute("fail", "Please login.");
	        return "redirect:/login";
	    }

	    Order order = customerService.getOrderById(orderId);
	    if (order == null || !order.getCustomer().getId().equals(customer.getId())) {
	        session.setAttribute("fail", "Unauthorized or invalid order.");
	        return "redirect:/customer/my-orders";
	    }

	    model.addAttribute("order", order);
	    return "order-invoice.html"; // ✅ View that renders the receipt
	}
	public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
	    Optional<Order> optionalOrder = orderRepository.findById(orderId);
	    if (optionalOrder.isPresent()) {
	        Order order = optionalOrder.get();
	        order.setStatus(newStatus); // ✅ Pass enum directly
	        orderRepository.save(order);
	    }
	}
	@GetMapping("/product/{id}")
	public String viewProductDetails(@PathVariable Long id, Model model) {
	    product p = customerService.findById(id); // Assuming you have a method like this
	    if (p == null) {
	        return "redirect:/customer/home"; // fallback
	    }
	    model.addAttribute("product", p);
	    return "product-details";
	}










}