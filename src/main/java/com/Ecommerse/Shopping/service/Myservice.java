package com.Ecommerse.Shopping.service;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.Ecommerse.Shopping.config.AES;
import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.entity.product;
import com.Ecommerse.Shopping.repository.CustomerRepository;
import com.Ecommerse.Shopping.repository.ProductRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class Myservice {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	@Value("${admin.email}")
	private String adminEmail;
	@Value("${admin.password}")
	private String adminPassword;

	public String login(String email, String password, HttpSession session) {
		if (email.equals(adminEmail) && password.equals(adminPassword)) {
			session.setAttribute("admin", "admin");
			session.setAttribute("pass", "Login Success, Welcome Admin");
			return "redirect:/admin/home";
		} else {
			Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

			if (optionalCustomer.isEmpty()) {
			    session.setAttribute("fail", "Invalid Email");
			    return "redirect:/login";
			} else {
			    Customer customer = optionalCustomer.get();
			    try {
			        String decrypted = AES.decrypt(customer.getPassword());
			        if (decrypted != null && decrypted.equals(password)) {
			            session.setAttribute("pass", "Login Success, Welcome " + customer.getFullname());
			            session.setAttribute("customer", customer);
			            return "redirect:/customer/home";
			        } else {
			            session.setAttribute("fail", "Invalid Password Try Again");
			            return "redirect:/login";
			        }
			    } catch (Exception e) {
			        e.printStackTrace();
			        session.setAttribute("fail", "Error decrypting password. Contact support.");
			        return "redirect:/login";
			    }
			}

		}
	}

	public void removeMessage() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession(true);
		session.removeAttribute("pass");
		session.removeAttribute("fail");
	}

	public String logout(HttpSession session) {
		session.removeAttribute("admin");
		session.removeAttribute("pass");
		session.removeAttribute("fail");
		session.setAttribute("fail", "Logout Success");
		return "redirect:/";
	}

	public String loadProductsForCustomer(HttpSession session, ModelMap model, String name, String sortParam,
			boolean desc, String stock, int page, int size) {

		Sort sort = Sort.by("name"); // default sort

		switch (sortParam) {
		case "price-asc":
			sort = Sort.by("price").ascending();
			break;
		case "price-desc":
			sort = Sort.by("price").descending();
			break;
		case "stock-desc":
			sort = Sort.by("stock").descending();
			break;
		default:
			sort = Sort.by("name").ascending();
		}

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<product> productPage;

		if ("in".equalsIgnoreCase(stock)) {
			productPage = productRepository.findByNameContainingIgnoreCaseAndStockGreaterThan(name, 0, pageable);
		} else {
			productPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
		}

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", productPage.getTotalPages());

// Preserve filter values
		model.addAttribute("name", name);
		model.addAttribute("sort", sortParam);
		model.addAttribute("stock", stock);
		model.addAttribute("desc", desc);
		model.addAttribute("size", size);

		return "main";
	}

}
