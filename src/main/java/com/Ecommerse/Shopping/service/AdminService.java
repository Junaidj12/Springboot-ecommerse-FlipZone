package com.Ecommerse.Shopping.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Ecommerse.Shopping.entity.product;
import com.Ecommerse.Shopping.exception.NotLoggedInException;
import com.Ecommerse.Shopping.repository.ProductRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {
	
	@Autowired
	ProductRepository productRepository; 
	
	@Value("${CLOUDINARY_URL}")
	private String url;


	public String loadhome(HttpSession session) {
		isLoggedIn(session);
		return "admin-home.html";
	}
	


	public String loadAddProduct(HttpSession session) {
		isLoggedIn(session);
		return "add-product.html";
		
	}

	public String AddProduct(product product, HttpSession session) {
		isLoggedIn(session);
		product.setImgLink(saveToCloud(product.getImage()));
		productRepository.save(product);
		session.setAttribute("pass", "Product Added Sucessfully !");
		return "redirect:/admin/home";
		
	}
	private String saveToCloud(MultipartFile image) {
	    Cloudinary cloudinary = new Cloudinary(url);
	    try {
	        Map<String, String> options = ObjectUtils.asMap("folder", "flipzone");
	        Map<String, String> data = cloudinary.uploader().upload(image.getBytes(), options);
	        return (String) data.get("url");  // or "secure_url"
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "";
	    }
	}




	public void isLoggedIn(HttpSession session) {
		if(session.getAttribute("admin")==null)
			throw new NotLoggedInException();
	}



	public void updateProduct(product updatedProduct, HttpSession session) {
	    isLoggedIn(session);
	    
	    product existing = productRepository.findById(updatedProduct.getId()).orElse(null);
	    
	    if (existing != null) {
	        existing.setName(updatedProduct.getName());
	        existing.setDescription(updatedProduct.getDescription());
	        existing.setPrice(updatedProduct.getPrice());
	        existing.setStock(updatedProduct.getStock());
	        productRepository.save(existing);
	        session.setAttribute("pass", "Product Updated Successfully!");
	    } else {
	        session.setAttribute("fail", "Product not found!");
	    }
	}


}
