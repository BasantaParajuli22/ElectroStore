package com.example.springTrain.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.JwtRespnose;
import com.example.springTrain.dto.LoginRequest;
import com.example.springTrain.dto.UserDto;
import com.example.springTrain.service.UserService;
import com.example.springTrain.util.JwtUtil;



@RestController
public class AuthController {

	Logger logger = LoggerFactory.getLogger(AuthController.class);
	private UserService userService; 
	private AuthenticationManager authenticationManager; //to authenticate user
	private JwtUtil jwtUtil; //to generate token
	
	public AuthController( UserService userService,
			AuthenticationManager authenticationManager,
			JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userService= userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registerNewUser(@RequestBody UserDto userDto) {
		
		
		logger.info(userDto.getEmail());
		logger.info("role "+ userDto.getRole());
		logger.info(userDto.getUsername());
		try {
			userService.createUser(userDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(400).body("body cannot be null");
		}
		return ResponseEntity.ok("user created successfully");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
		
		logger.info(loginRequest.getEmail());
		logger.info(loginRequest.getPassword());
		
		//The UsernamePasswordAuthenticationToken is created with the username and password from the LoginRequest.
		//The authenticationManager.authenticate() method is called to authenticate the user.
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
				);
		logger.info("authentication is "+ authentication);

		//By setting the authentication object in the SecurityContext, the user is now considered authenticated for the current request.
		//This allows Spring Security to perform authorization checks (e.g., @PreAuthorize) for subsequent operations
		SecurityContextHolder.getContext().setAuthentication(authentication);
	
		try {		
			//The authentication.getPrincipal() method returns the UserDetails object representing the authenticated user.
			//then using UserDetails to generate token
			String jwt = jwtUtil.generateToken( (UserDetails) authentication.getPrincipal());
			return ResponseEntity.ok(new JwtRespnose(jwt)) ;//returring token
		} catch (Exception e) {
			logger.info("Validation of token exception "+ e);
			return ResponseEntity.status(400).build();
		}
		
		
	}
	
	
}
