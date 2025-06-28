package com.Ecommerse.Shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Ecommerse.Shopping.repository.ProductRepository;
import com.Ecommerse.Shopping.service.Myservice;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyController {
	
	@Autowired
	Myservice myservice;
	
	
	MyController(CustomerController customerController) {
	}
	
	@Autowired
	ProductRepository productRepository;

	@GetMapping("/")
	public String loadCustomerHome(
	        HttpSession session,
	        ModelMap model,
	        @RequestParam(defaultValue = "") String name,
	        @RequestParam(defaultValue = "name") String sort,
	        @RequestParam(defaultValue = "false") boolean desc,
	        @RequestParam(defaultValue = "all") String stock,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "6") int size
	) {

	    return myservice.loadProductsForCustomer(session, model, name, sort, desc, stock, page, size);
	}



	@GetMapping("/login")
	public String loadLogin(HttpSession session) {
	    return "login.html";
	}
	@PostMapping("/login")
	public String adminlogin(@RequestParam String email ,@RequestParam String password, HttpSession session) {
		return myservice.login(email, password, session);
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		return myservice.logout(session);
	}
	@GetMapping("/clear-toast")
	@ResponseBody
	public void clearToast(HttpSession session) {
	    session.removeAttribute("pass");
	    session.removeAttribute("fail");
	}
}
