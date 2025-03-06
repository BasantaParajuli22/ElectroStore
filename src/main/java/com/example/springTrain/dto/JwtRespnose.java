package com.example.springTrain.dto;

public class JwtRespnose {

	private String jwt;
	
	public JwtRespnose(String jwt) {
		this.jwt = jwt;
	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	
	
}
