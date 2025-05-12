package com.example.springTrain.dto;

public class JwtAuthenticationResponse {
    private String accessToken;
    
    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    // getter

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
    
    
}