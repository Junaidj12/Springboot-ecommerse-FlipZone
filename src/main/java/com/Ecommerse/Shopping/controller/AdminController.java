package com.Ecommerse.Shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Ecommerse.Shopping.entity.product;
import com.Ecommerse.Shopping.repository.ProductRepository;
import com.Ecommerse.Shopping.service.AdminService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	ProductRepository productRepository;

	@GetMapping("/home")
	public String adminHome(HttpSession session) {
		
		return adminService.loadhome(session);
	}
	
	@GetMapping("/add-product")
	public String loadAddProduct(HttpSession session) {
		return adminService.loadAddProduct(session);
	}

	@PostMapping("/add-product")
	public String AddProduct(@ModelAttribute product product,HttpSession session) {
		return adminService.AddProduct(product,session);
	}

	@GetMapping("/manage-products")
	public String showAllProducts(ModelMap modelMap, HttpSession session) {
	   return adminService.manageProduct(modelMap,session);
	}
	@GetMapping("/edit-product")
	public String editProduct(HttpSession session,@RequestParam Long id, ModelMap map) {
	    return adminService.editProduct(session, id, map);
	}
	@PostMapping("/update-product")
	public String updateProduct(@ModelAttribute product updatedProduct, HttpSession session) {
	   return adminService.updateProduct(updatedProduct, session);
	}
	@GetMapping("/delete-product")
	public String deleteProduct(@RequestParam Long id, HttpSession session) {
	   return adminService.deleteProduct(id, session);
	}

	



	
}
