package com.example.springTrain.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springTrain.model.User;
import com.example.springTrain.repository.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(username)
				.orElseThrow( () -> new UsernameNotFoundException("email not found in UserDetails"));
		 
        System.out.println(user.getRole().name());

		 return org.springframework.security.core.userdetails.User
				 .withUsername(user.getEmail())//setting email as username 
				 .password(user.getPassword())
				 .roles(user.getRole().name())
				 .build();
	}

}
