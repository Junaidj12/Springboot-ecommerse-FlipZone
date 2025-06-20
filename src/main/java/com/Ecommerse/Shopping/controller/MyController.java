package com.Ecommerse.Shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Ecommerse.Shopping.service.Myservice;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyController {
	
	@Autowired
	Myservice myservice;
	
	
	MyController(CustomerController customerController) {
	}
	
	@GetMapping("/")
	public String loadMain() {
		return "main";
	}
	@GetMapping("/login")
	public String loadLogin() {
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
}
