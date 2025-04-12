package com.example.springTrain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springTrain.dto.ProductDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.exceptions.InvalidInputException;
import com.example.springTrain.exceptions.ResourceNotFoundException;
import com.example.springTrain.model.Product;
import com.example.springTrain.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

	Logger logger = LoggerFactory.getLogger(ProductService.class);

    private ProductRepository productRepository;
    private FileStorageService fileStorageService;
    
    public ProductService(ProductRepository productRepository,
    		FileStorageService fileStorageService) {
    	this.fileStorageService =fileStorageService;
    	this.productRepository = productRepository;
    }

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

    public String getSavedFileName(MultipartFile file) {
    	if(file == null) {
    		logger.warn("product image file is null so setting to default image");
    		return "default-img.png";//default product image
    	}
    	String savedFileName = fileStorageService.saveFile(file);
    	return savedFileName;
    }
    
    
    @Transactional
    public Product createProduct(ProductDto productDto,MultipartFile file) {
    	if(productDto == null ) {
    		throw new IllegalArgumentException("productDto has null values");
    	}
    	
    	String savedFileName = getSavedFileName(file);
    	try {
    		Product product = new Product();
    		product.setCategory(productDto.getCategory());
    		product.setDescription(productDto.getDescription());
    		product.setName(productDto.getName());
    		product.setPrice(productDto.getPrice());
    		product.setStockQuantity(productDto.getStockQuantity());
    		product.setImageName(savedFileName);
    		return productRepository.save(product);	
		} catch (Exception e) {
            throw new CreationFailedException("Customer Creation Failed " + e.getMessage());
		}
    }
    
    @Transactional
	public Product createProductOnly(ProductDto productDto) {
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
