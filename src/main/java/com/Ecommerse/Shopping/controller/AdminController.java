package com.Ecommerse.Shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Ecommerse.Shopping.entity.Order;
import com.Ecommerse.Shopping.entity.OrderStatus;
import com.Ecommerse.Shopping.entity.product;
import com.Ecommerse.Shopping.repository.ProductRepository;
import com.Ecommerse.Shopping.service.AdminService;
import com.Ecommerse.Shopping.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	OrderService orderService;
	
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
	public String showAllProducts(
	        @RequestParam(defaultValue = "") String name,
	        @RequestParam(defaultValue = "price") String sort,
	        @RequestParam(defaultValue = "false") boolean desc,
	        @RequestParam(defaultValue = "all") String stock,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "6") int size,
	        ModelMap modelMap,
	        HttpSession session) {
	    
	    return adminService.manageProduct(modelMap, session, name, sort, desc, stock, page, size);
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
	@GetMapping("/manage-orders")
	public String manageOrders(Model model) {
	    List<Order> orders = orderService.getAllOrders();
	    model.addAttribute("orders", orders);
	    return "manage-orders";
	}

	@PostMapping("/update-status/{orderId}")
	public String updateOrderStatus(@PathVariable Long orderId,
	                                @RequestParam("status") OrderStatus status) {
	    orderService.updateOrderStatus(orderId, status);
	    return "redirect:/admin/manage-orders";
	}
	@GetMapping("/dashboard")
	public String viewDashboard(ModelMap modelMap) {
	    long totalOrders = adminService.getTotalOrders();
	    double totalSales = adminService.getTotalSales();

	    modelMap.addAttribute("totalOrders", totalOrders);
	    modelMap.addAttribute("totalSales", totalSales);

	    return "admin-dashboard";
	}


	
}