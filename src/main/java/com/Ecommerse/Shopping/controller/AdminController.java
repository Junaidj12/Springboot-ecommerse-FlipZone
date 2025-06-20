package com.Ecommerse.Shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Ecommerse.Shopping.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;

	@GetMapping("/home")
	public String adminHome(HttpSession session) {
		return AdminService.loadhome(session);
	}
	
	@GetMapping("/add-product")
	public String loadAddProduct() {
		return "add-product";
	}

	@GetMapping("/view-products")
	public String viewProducts() {
		return "view-product.html";
	}

	@GetMapping("/edit-product")
	public String editProduct() {
		return "edit-product.html";
	}
	
}
