package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.CustomerDto;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Order;
import com.example.springTrain.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id,
    		Authentication authentication) {
        
    	System.out.println("getCustomerById in controller "+ id);
    	 System.out.println("Authentication object: " + authentication);
    	try {
			customerService.authenticateCustomer(id, authentication);
			Customer customer= customerService.getCustomerById(id);
			return ResponseEntity.ok(customer);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
    	
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id,
    		Authentication authentication,
    		@RequestBody CustomerDto customerDto) {
       
    	try {
			customerService.authenticateCustomer(id, authentication);
	    	Customer customer = customerService.updateCustomer(id, customerDto);
			return ResponseEntity.ok(customer);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
    	
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long id, Authentication authentication) {
    	try {
			customerService.authenticateCustomer(id, authentication);
	    	customerService.deleteCustomer(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable("id") Long id, Authentication authentication) {
    	try {
			customerService.authenticateCustomer(id, authentication);
	    	List<Order> order = customerService.getOrdersByCustomerId(id);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
    }
}
