package com.example.springTrain.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthorization {
	
	  // getting logged-in username if it is logged in & authenticated
    public static String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();  // Get the username of the logged-in user
        }
        return null;  // Return null if not authenticated
    }

	public static String getLoggedInUserRole() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.getAuthorities() != null) {
	        return authentication.getAuthorities().stream()
	            .findFirst() // Assuming single role per user
	            .map(GrantedAuthority::getAuthority)
	            .orElse(null);
	    }
	    return null;
	}

}