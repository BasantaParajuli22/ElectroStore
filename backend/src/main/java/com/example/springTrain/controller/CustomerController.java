package com.example.springTrain.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.CustomerDto;
import com.example.springTrain.dto.UserDto;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Order;
import com.example.springTrain.model.User;
import com.example.springTrain.service.CustomerService;
import com.example.springTrain.service.UserService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;

    public CustomerController(CustomerService customerService,
    		UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
		Customer customer= customerService.getCustomerById(id);
		return ResponseEntity.ok(customer);
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getMyUserProfile() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
     
        try {
        	User user = userService.findByUserEmail(email);
        	Customer customer = customerService.getCustomerByUserId(user.getId());

        	CustomerDto customerDto = customerService.getCustomerDtoById(customer.getId());
        	
            return ResponseEntity.ok(customerDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Long id,
    		@RequestBody CustomerDto customerDto) {
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String email = authentication.getName(); // Get the username (email) from the token
         User user = userService.findByUserEmail(email);
         if (user == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         
    	CustomerDto customer = customerService.updateCustomer(id, customerDto);
		return ResponseEntity.ok(customer);
	
    	
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long userId) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
    	customerService.deleteCustomer(userId);
		return ResponseEntity.noContent().build();
	
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable("id") Long id) {
    	List<Order> order = customerService.getOrdersByCustomerId(id);
		return ResponseEntity.ok(order);
	
    }
}
