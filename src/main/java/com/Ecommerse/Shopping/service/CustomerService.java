package com.Ecommerse.Shopping.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.Ecommerse.Shopping.config.AES;
import com.Ecommerse.Shopping.entity.CartItem;
import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.entity.product;
import com.Ecommerse.Shopping.exception.NotLoggedInException;
import com.Ecommerse.Shopping.repository.CartItemRepository;
import com.Ecommerse.Shopping.repository.CustomerRepository;
import com.Ecommerse.Shopping.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomerService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	public String register(Customer customer, HttpSession session) {
		if (customerRepository.existsByEmail(customer.getEmail())
				|| customerRepository.existsByMobile(customer.getMobile())) {
			session.setAttribute("fail", "* Account Already Exists");
			return "redirect:/customer/register";

		} else {
			customer.setPassword(AES.encrypt(customer.getPassword()));
			int otp = new Random().nextInt(100000, 1000000);
			session.setAttribute("otp", otp);
			session.setAttribute("register", customer);
			sendotp(otp, customer.getEmail());
			session.setAttribute("pass", "Otp Sent Successfully!");
			return "redirect:/customer/otp";
		}
	}

	private void sendotp(int otp, String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("OTP Verification");
		message.setText("Your OTP is " + otp + " , Enter this otp to create Account");
		try {
			mailSender.send(message);
		} catch (MailAuthenticationException e) {
			System.err.println("Sending Mail Failed but OTP is : " + otp);
		}
	}

	public String submitOtp(int otp, HttpSession session) {
		int generatedOtp = (int) session.getAttribute("otp");
		Customer customer = (Customer) session.getAttribute("register");
		if (otp == generatedOtp) {
			customerRepository.save(customer);
			session.setAttribute("pass", "Account Created Success");
			return "redirect:/login";
		} else {
			session.setAttribute("fail", "Invalid Otp, Try Again");
			return "redirect:/customer/otp";
		}
	}

	public String loadHome(HttpSession session) {
		getCustomerFromSession(session);
		return "customer-home.html";
	}

	public Customer getCustomerFromSession(HttpSession session) {
		if (session.getAttribute("customer") == null)
			throw new NotLoggedInException();
		else
			return (Customer) session.getAttribute("customer");
	}

	public String loadProductsForCustomer(HttpSession session, ModelMap model, String name, String sortParam,
			boolean desc, String stock, int page, int size) {
		String sortField = "name"; // default
		Sort.Direction direction = Sort.Direction.ASC; // default

// Map frontend sortParam to field & direction
		switch (sortParam) {
		case "price-asc":
			sortField = "price";
			direction = Sort.Direction.ASC;
			break;
		case "price-desc":
			sortField = "price";
			direction = Sort.Direction.DESC;
			break;
		case "stock":
			sortField = "stock";
			direction = Sort.Direction.DESC;
			break;
		case "name":
		default:
			sortField = "name";
			direction = Sort.Direction.ASC;
			break;
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

		Page<product> productPage;
		if ("in".equalsIgnoreCase(stock)) {
			productPage = productRepository.findByNameContainingIgnoreCaseAndStockGreaterThan(name, 0, pageable);
		} else {
			productPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
		}

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", productPage.getTotalPages());

// Preserve filters for pagination
		model.addAttribute("name", name);
		model.addAttribute("sort", sortParam);
		model.addAttribute("stock", stock);
		model.addAttribute("size", size);

		return "customer-home.html";
	}

	public String addToCart(Long productId, HttpSession session) {
	    Customer customer = (Customer) session.getAttribute("customer");
	    if (customer == null) {
	        session.setAttribute("fail", "Please login to add items to cart.");
	        return "redirect:/login";
	    }

	    product product = productRepository.findById(productId).orElse(null);
	    if (product == null) {
	        session.setAttribute("fail", "Product not found.");
	        return "redirect:/customer/home";
	    }

	    CartItem existing = cartItemRepository.findByCustomerAndProduct(customer, product);
	    if (existing != null) {
	        existing.setQuantity(existing.getQuantity() + 1);
	        cartItemRepository.save(existing);
	    } else {
	        CartItem item = new CartItem();
	        item.setCustomer(customer);
	        item.setProduct(product);
	        item.setQuantity(1);
	        cartItemRepository.save(item);
	    }

	    session.setAttribute("pass", "Added to cart successfully!");
	    return "redirect:/customer/home";
	}


	public List<CartItem> getCartItems(Customer customer) {
		return cartItemRepository.findByCustomer(customer);
	}

	public long calculateTotal(List<CartItem> cartItems) {
	    final long[] total = {0};
	    cartItems.forEach(item -> {
	        total[0] += item.getQuantity() * item.getProduct().getPrice();
	    });
	    return total[0];
	}

	public void updateCartQuantity(Customer customer, Long productId, int change) {
	    CartItem item = cartItemRepository.findByCustomerAndProduct(customer, productRepository.findById(productId).orElse(null));
	    if (item != null) {
	        int newQty = item.getQuantity() + change;
	        if (newQty <= 0) {
	            cartItemRepository.delete(item);
	        } else {
	            item.setQuantity(newQty);
	            cartItemRepository.save(item);
	        }
	    }
	}






}
