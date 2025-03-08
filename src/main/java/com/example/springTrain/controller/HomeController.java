package com.example.springTrain.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the Home Page!";
    }

    @GetMapping("/login")
    public String login() {
        return "Login Page";
    }

    @GetMapping("/register")
    public String register() {
        return "Registration Page";
    }

    @GetMapping("/home")
    public String homePage() {
        return "This is the Home Page!";
    }
    
    @GetMapping("/api/admin/protected")
    public String adminProtectedPage() {
        return "This is the protected Page! only for admin";
    }
    
    @GetMapping("/api/customer/protected")
    public String protectedPage() {
        return "only customer can access this";
    }
    
    
}