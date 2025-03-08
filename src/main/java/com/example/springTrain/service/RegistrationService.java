package com.example.springTrain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.springTrain.controller.AuthController;
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
  
	
}
