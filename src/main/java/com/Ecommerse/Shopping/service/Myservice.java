package com.Ecommerse.Shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.Ecommerse.Shopping.config.AES;
import com.Ecommerse.Shopping.entity.Customer;
import com.Ecommerse.Shopping.repository.CustomerRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class Myservice {
	
	@Autowired
	CustomerRepository customerRepository;
	
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
	        Customer customer = customerRepository.findByEmail(email);
	        if (customer == null) {
	            session.setAttribute("fail", "Invalid Email");
	            return "redirect:/login";
	        } else {
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


	

}
