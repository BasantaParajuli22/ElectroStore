package com.example.springTrain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.UserDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.model.User;
import com.example.springTrain.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    	this.userRepository = userRepository;
    	this.passwordEncoder = passwordEncoder;
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User createUser(UserDto userDto) throws Exception {
    	
    	if(userDto == null) {
    		throw new Exception("User cannot null");
    	}
    	Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
    	if(existingEmail.isPresent()) {
    		throw new RuntimeException("Email already exists");
    	}
    	try {
    		User user =new User();
    		user.setEmail(userDto.getEmail());
    		user.setUsername(userDto.getUsername());
    		user.setPassword(passwordEncoder.encode( userDto.getPassword()) );    	
    		user.setRole(userDto.getRole());
    		
    		return userRepository.save(user);	
		} catch (Exception e) {
			throw new CreationFailedException("Email already exists");
		}
    	
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
    	userRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("User not found"));
    	
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) throws Exception {
    	userRepository.findById(id)
		.orElseThrow(() -> new Exception("User not found"));
    	
        userRepository.deleteById(id);
    }
}
