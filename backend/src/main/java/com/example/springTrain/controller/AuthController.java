package com.example.springTrain.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.JwtAuthenticationResponse;
import com.example.springTrain.dto.LoginRequest;
import com.example.springTrain.dto.UserDto;
import com.example.springTrain.exceptions.UnauthorizedAccessException;
import com.example.springTrain.security.JwtTokenProvider;
import com.example.springTrain.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {	

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
    		JwtTokenProvider tokenProvider,
    		UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    //login 
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    
    //add new user
    @PostMapping("/add")
    public ResponseEntity<?> registerNewUser(@RequestBody UserDto user) {
    	try {
    		UserDto newUser = userService.createUser(user);
    		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    	} catch (Exception e) {
    		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(
        @RequestParam("email") String email, 
        @RequestParam("password") String password) {
        
        try {
            // Authenticate user
            UserDto user = userService.authenticateUser(email, password);
            
            // Create response body
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", email);
            response.put("username", user.getUsername());  
            response.put("roles", user.getRole());     
            return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
                
        } catch (UnauthorizedAccessException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("error", "Invalid credentials"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Server error"));
        }
    }

}
