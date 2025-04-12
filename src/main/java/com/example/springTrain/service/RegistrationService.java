package com.example.springTrain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.CustomerDto;
import com.example.springTrain.dto.UserDto;
import com.example.springTrain.model.User;

import jakarta.transaction.Transactional;

@Service
public class RegistrationService {

	Logger logger = LoggerFactory.getLogger(RegistrationService.class);
	private final UserService userService; 
    private final CustomerService customerService;

    public RegistrationService(CustomerService customerService,
    		UserService userService) {
        this.userService = userService;
        this.customerService = customerService;
    }
  
    @Transactional
	public void createUserAndCustomer(UserDto userDto, CustomerDto customerDto) {

		try {
			User user = userService.createUser(userDto);
			customerService.createCustomer(customerDto, user);
		} catch (Exception e) {	
			logger.error("error caught while creating user"+ e);
		}
	}

	public void createUser(UserDto userDto) {
		try {
			User user = userService.createUser(userDto);
		} catch (Exception e) {	
			logger.error("error caught while creating user"+ e);
		}
		
	}

	public void createUserDetails(CustomerDto customerDto) {	
		try {
            // Get the currently authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            
            // Find the user in database
            User user = userService.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Create customer details for the user
            customerService.createCustomer(customerDto, user);
            logger.info("Successfully created customer details for user: {}", currentUsername);
        } catch (Exception e) {
            logger.error("Error creating customer details", e);
            throw new RuntimeException("Failed to create customer details", e);
        }
	}
  
	
}
