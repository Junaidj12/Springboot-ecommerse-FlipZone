package com.Ecommerse.Shopping.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {

	public static String loadhome(HttpSession session) {
		if(isLoggedIn(session))
			return "admin-home.html";
		else
			return "redirect:/login";		
	}
	
	private static boolean isLoggedIn(HttpSession session) {
		if (session.getAttribute("admin") != null)
			return true;
		else
			return false;
	}

}
