package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springTrain.dto.ProductDto;
import com.example.springTrain.enums.ProductCategory;
import com.example.springTrain.model.Product;
import com.example.springTrain.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
    	List<Product> productList = productService.getAllProducts();
         return ResponseEntity.ok(productList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    //create a product with details
    @PostMapping(value ="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Product> createProductAndImage(
    		@RequestParam("file") MultipartFile file,
    		@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("category") ProductCategory category,
            @RequestParam("stock") int quantity) {
    	
	    	ProductDto productDto = new ProductDto();
	    	productDto.setName(name);
	    	productDto.setDescription(description);
	    	productDto.setPrice(price);
    	    productDto.setCategory(category);
    	    productDto.setStockQuantity(quantity);
    	    
        Product createdProduct = productService.createProduct(productDto,file);
        return ResponseEntity.ok(createdProduct);
    }

    @PostMapping
    public ResponseEntity<Product> createProductOnly(@RequestBody ProductDto productDto) {
        Product createdProduct = productService.createProductOnly(productDto);
        return ResponseEntity.ok(createdProduct);
    }
    
    //for file upload and save to db and get Saved filename
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
//        
//    	System.out.println("Received file: "+ file.getOriginalFilename() +" "+ file.getContentType());
//    	String uploadedFile = productService.getSavedFileName(file);
//        return ResponseEntity.ok(uploadedFile);
//    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id,
    		@RequestBody ProductDto product) {
    	Product updatedProduct = productService.updateProduct(id, product);
    	return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}