package com.employee.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTCredentialsFilter extends UsernamePasswordAuthenticationFilter{

	private AuthenticationManager manager;
	
	public JWTCredentialsFilter(AuthenticationManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		Authentication auth = null;
		
		try {
			UserDetails authReq = new ObjectMapper().readValue(request.getInputStream(), UserDetails.class);
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(authReq.getPassword(), authReq.getPassword());
			
			auth = manager.authenticate(authentication);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return auth;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String key = "securekeysecurekeysecurekeysecurekeysecurekeysecurekeysecurekeysecurekeysecurekeysecurekey";
		
		String token = Jwts.builder()
			.setSubject(authResult.getName())
			.claim("authorities", authResult.getAuthorities())
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
			.signWith(Keys.hmacShaKeyFor(key.getBytes()))
			.compact();
		
		response.addHeader("Authorization", "Bearer " +token);
	}
}
