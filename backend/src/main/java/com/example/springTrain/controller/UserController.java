package com.example.springTrain.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.UserDto;
import com.example.springTrain.exceptions.UnauthorizedAccessException;
import com.example.springTrain.service.UserService;


@RestController
@RequestMapping(path = "api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;    
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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

    @PostMapping("/add")
    public ResponseEntity<?> registerNewUser(@RequestBody UserDto user) {
        try {
            UserDto newUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserByEmail(@RequestParam("email") String email) {
        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
  
}
