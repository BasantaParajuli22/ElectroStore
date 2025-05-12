package com.example.springTrain.security;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.springTrain.enums.UserRole;
import com.example.springTrain.model.User;
import com.example.springTrain.repository.UserRepository;


@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin@admin.com";
            String adminPassword = "admin123";
            // Check if user with role ADMIN exists in the repository
            Optional<User> existingAdmin = userRepository.findByEmail(adminUsername);
            
            if (existingAdmin == null) {  // If no admin is found, create one
                User admin = new User();
                admin.setEmail(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword)); // Password encoding
                admin.setRole(UserRole.ADMIN);  // Assign role as Admin
                userRepository.save(admin);  // Save to the database
            }
        };
    }
}