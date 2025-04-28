package com.example.springTrain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.UserDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.exceptions.UnauthorizedAccessException;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.User;
import com.example.springTrain.repository.CustomerRepository;
import com.example.springTrain.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    public UserService(UserRepository userRepository,
    		PasswordEncoder passwordEncoder,
    		CustomerRepository customerRepository) {
    	this.userRepository = userRepository;
    	this.passwordEncoder = passwordEncoder;
    	this.customerRepository = customerRepository;
    }
    
    private UserDto convertToUserDto(User user) {
    	UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        
        return dto;
    }
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
        		.map(this::convertToUserDto)
        		.toList();
    }

    public UserDto getUserById(Long id) {
    	User user = userRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("User not found by id"));
    	return convertToUserDto(user);
    }
    
	public UserDto findByEmail(String email) {
		User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found BY EMAIL"));
		return convertToUserDto(user);
	}
	
	public User findByUserEmail(String email) {
		User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found BY EMAIL"));
		return user;
	}
	
	public UserDto authenticateUser(String email, String password) {
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UnauthorizedAccessException("Invalid email or password"));
	    
	    // Use passwordEncoder.matches() instead of equals()
	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new UnauthorizedAccessException("Invalid email or password");
	    }    
	    return convertToUserDto(user);
	}

	@Transactional
	public UserDto createUser(UserDto userDto) throws Exception {
	    if (userDto == null) {
	        throw new Exception("User cannot be null");
	    }

	    Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
	    if (existingEmail.isPresent()) {
	        throw new RuntimeException("Email already exists");
	    }

	    try {
	        User user = new User();
	        user.setEmail(userDto.getEmail());
	        user.setUsername(userDto.getUsername());
	        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
	        user.setRole(userDto.getRole());
	        
	        User savedUser = userRepository.save(user);

	        // IF Role is CUSTOMER, create Customer linked to this User
	        if (savedUser.getRole().name().equals("CUSTOMER")) {
	            Customer customer = new Customer();
	            customer.setUser(savedUser);
	            // You can leave customer fields blank here, or later update
	            customerRepository.save(customer);
	        }

	        return convertToUserDto(savedUser);
	    } catch (Exception e) {
	        throw new CreationFailedException("User creation failed: " + e.getMessage());
	    }
	}


    @Transactional
    public UserDto updateUser(Long id, User userDetails) throws Exception {
		userRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("User not found"));
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> new Exception("User not found"));
		user.setUsername(userDetails.getUsername());
		user.setPassword(passwordEncoder.encode( userDetails.getPassword() ));
		user.setEmail(userDetails.getEmail());
		user.setRole(userDetails.getRole());
		user.setUpdatedAt(LocalDateTime.now());
		User savedUser = userRepository.save(user);
		
		return convertToUserDto(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) throws Exception {
    	userRepository.findById(id)
			.orElseThrow(() -> new Exception("User not found"));
        userRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email: " + email + " doesn't exist"));
        userRepository.deleteById(user.getId());
    }
    
}
