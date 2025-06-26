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
import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.entity.product;
import com.Ecommerse.Shopping.exception.NotLoggedInException;
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

	public String loadProductsForCustomer(HttpSession session, ModelMap model, String name, String sort, boolean desc,
			String stock, int page, int size) {

		Sort sorting = desc ? Sort.by(sort).descending() : Sort.by(sort);
		Pageable pageable = PageRequest.of(page, size, sorting);

		Page<product> productPage;

		if (stock.equals("in")) {
			productPage = productRepository.findByNameContainingIgnoreCaseAndStockGreaterThan(name, 0, pageable);
		} else {
			productPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
		}

		model.addAttribute("productList", productPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", productPage.getTotalPages());
		return "customer-home.html";
	}

}
