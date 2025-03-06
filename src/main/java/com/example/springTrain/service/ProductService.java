package com.example.springTrain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springTrain.model.Product;
import com.example.springTrain.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
    	
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
    	productRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Product not found"));
    	
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
    	productRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Product not found"));
    	
        productRepository.deleteById(id);
    }
}
