package com.Ecommerse.Shopping.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
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
			Map<String, String> map = ObjectUtils.asMap("folder", "flipzone");
			Map<String, String> data = cloudinary.uploader().upload(image.getBytes(), map);
			return (String) data.get("url");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void isLoggedIn(HttpSession session) {
		if (session.getAttribute("admin") == null)
			throw new NotLoggedInException();
	}

	public String updateProduct(product updatedProduct, HttpSession session) {
		isLoggedIn(session);
		try {
			if (updatedProduct.getImage().getInputStream().available() > 0) {
				updatedProduct.setImgLink(saveToCloud(updatedProduct.getImage()));
			} else {
				updatedProduct.setImgLink(productRepository.findById(updatedProduct.getId()).get().getImgLink());
			}
			productRepository.save(updatedProduct);
			session.setAttribute("pass", "Product Updated Secessfully");
			return "redirect:/admin/manage-products";
		} catch (IOException e) {

			e.printStackTrace();
			return "redirect:/admin/home";
		}

	}

	public String manageProduct(ModelMap modelMap, HttpSession session) {
		isLoggedIn(session);
		List<product> products = productRepository.findAll();
		if (products.isEmpty()) {
			session.setAttribute("fail", "No Products Found");
			return "redirect:/admin/home";
		} else {
			modelMap.put("products", products);
			return "manage-product.html";
		}
	}

	public String deleteProduct(Long id, HttpSession session) {
		isLoggedIn(session);
		productRepository.deleteById(id);
		session.setAttribute("pass", "Product Deleted sucessfully");
		return "redirect:/admin/manage-products";
	}

	public String editProduct(HttpSession session, Long id, ModelMap map) {
		isLoggedIn(session);
		product product = productRepository.findById(id).orElse(null);
		map.put("product", product);
		return "edit-product.html";
	}

}
