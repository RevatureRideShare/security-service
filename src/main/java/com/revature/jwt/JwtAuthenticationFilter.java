package com.revature.jwt;

import com.auth0.jwt.JWT;
import com.revature.bean.Login;
import com.revature.bean.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.revature.jwt.JwtProperties;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//! The JwtAuthenticationFilter class uses the automatically generated /login mapping 
//! to authenticate the user when they login. JwtAuthenticationFilter extends the 
//! UsernamePasswordAuthenticationFilter class that exists in Spring Security.
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	// Method attempts to login a user.
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// Maps login request to login bean.
		Login login = null;

		try {
			login = new ObjectMapper().readValue(request.getInputStream(), Login.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creates the login authentication token.
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				login.getEmail(), login.getPassword(), new ArrayList<>());

		// Authenticate the user.
		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		return authentication;

	}

	// Method attaches JWT to response header if login is successful.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// Getting currently logged in user.
		UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();

		// Creating JWT token.
		String token = JWT.create().withSubject(userPrincipal.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.sign(HMAC512(JwtProperties.SECRET.getBytes()));

		// Adding JWT token in the response header.
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
	}
}
