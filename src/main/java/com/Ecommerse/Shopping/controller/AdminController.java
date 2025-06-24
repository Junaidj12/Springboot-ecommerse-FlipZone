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
	public String showAllProducts(ModelMap modelMap) {
	    List<product> products = productRepository.findAll();
	    modelMap.addAttribute("productList", products);
	    return "manage-product.html";
	}
	@GetMapping("/edit-product")
	public String editProduct(@RequestParam Long id, ModelMap map) {
	    product product = productRepository.findById(id).orElse(null);
	    map.addAttribute("product", product);
	    return "edit-product.html"; 
	}
	@PostMapping("/update-product")
	public String updateProduct(@ModelAttribute product updatedProduct, HttpSession session) {
	    adminService.updateProduct(updatedProduct, session);
	    return "redirect:/admin/manage-products";
	}
	@GetMapping("/delete-product")
	public String deleteProduct(@RequestParam Long id, HttpSession session) {
	    productRepository.deleteById(id);
	    session.setAttribute("pass", "Product deleted successfully!");
	    return "redirect:/admin/manage-products";
	}

	



	
}
