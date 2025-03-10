package com.example.springTrain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.ProductDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.exceptions.InvalidInputException;
import com.example.springTrain.exceptions.ResourceNotFoundException;
import com.example.springTrain.model.Product;
import com.example.springTrain.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
		 if (id == null) {
		     throw new InvalidInputException("Product ID cannot be null");
		 }
        return productRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product createProduct(ProductDto productDto) {
    	if(productDto == null ) {
    		throw new IllegalArgumentException("productDto has null values");
    	}
    	try {
    		Product product = new Product();
    		product.setCategory(productDto.getCategory());
    		product.setDescription(productDto.getDescription());
    		product.setName(productDto.getName());
    		product.setPrice(productDto.getPrice());
    		product.setStockQuantity(productDto.getStockQuantity());
    		return productRepository.save(product);	
		} catch (Exception e) {
            throw new CreationFailedException("Customer Creation Failed " + e.getMessage());
		}
    }
    
    @Transactional
    public Product updateProduct(Long id, ProductDto productDto) {
    	if (id == null || productDto == null) {
            throw new InvalidInputException("Invalid input: ID or productDto data cannot be null");
        }
    	productRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Product Id not found"));
    	
        Product product = getProductById(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setStockQuantity(productDto.getStockQuantity());
        return productRepository.save(product);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
    	 if (id == null) {
             throw new InvalidInputException("Product ID cannot be null");
         }
    	productRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    	
        productRepository.deleteById(id);
    }
}
