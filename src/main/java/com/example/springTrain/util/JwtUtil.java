package com.example.springTrain.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {
	Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("my-ultra-secure-256-bit-secret-key".getBytes());	final long EXPIRATION_TIME = 86400000;
	
	public String generateToken(UserDetails userDetails) {
		
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SECRET_KEY)
				.compact();	
	}

	
	public Boolean validateToken(String token, UserDetails userDetails) {
		try {
			
			String tokenUsername = extractUsername(token);
			Boolean b = isTokenExpired(token);
			
			return (( tokenUsername .equals(userDetails.getUsername()) ) 
					&& (b == false));
		} catch (SignatureException e) {
			logger.warn("error while validating token"+e);
			return false;
		}
	}

	public String extractUsername(String token) {
		
		try {
			
			return Jwts.parserBuilder()
					.setSigningKey(SECRET_KEY)
					.build()
					.parseClaimsJws(token)//to parse Claims Jws not parseClaimsJwt
					.getBody()
					.getSubject();
		} catch (Exception e) {
			logger.warn("error while extractUsername "+e);
			return "error found";
		}
				
	}
	
	private Boolean isTokenExpired(String token) {
		
		try {
			return Jwts.parserBuilder()
					.setSigningKey(SECRET_KEY)
					.build()
					.parseClaimsJws(token)//to parse Claims Jws
					.getBody()
					.getExpiration()
					.before(new Date());	
		} catch (Exception e) {
			logger.warn("error while extractUsername "+e);
			return true;
		}
		
		
	}

}


