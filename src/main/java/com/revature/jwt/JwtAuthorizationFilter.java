package com.revature.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.revature.bean.Security;
import com.revature.bean.UserPrincipal;
import com.revature.repository.SecurityRepository;

//! The JwtAuthorizationFilter class checks every request to ensure that a user 
//! performing a specific action has the appropriate permissions to perform each method.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private SecurityRepository securityRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SecurityRepository securityRepository) {
		super(authenticationManager);
		this.securityRepository = securityRepository;
	}

	// Filters requests to ensure that user has appropriate permissions before
	// performing methods.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Read the request header and look for the JWT token.
		String header = request.getHeader(JwtProperties.HEADER_STRING);

		// If header does not start with "BEARER " or is null let Spring handle it and
		// exit.
		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		// If the header is present, try to grab the logged in user from the database
		// and perform authorization.
		Authentication authentication = getEmailPasswordAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Continue the execution of the method.
		chain.doFilter(request, response);
	}

	// Checks the HTTP Request header to see if the JWT is there.
	private Authentication getEmailPasswordAuthentication(HttpServletRequest request) {
		String token = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");

		if (token != null) {
			// Parse the JWT and validate it matches the expected values.
			String email = JWT.require(HMAC512(JwtProperties.SECRET.getBytes())).build().verify(token).getSubject();

			// Search in the database, if we find the user by the token subject (email),
			// then grab the user details and create the Spring authentication token using
			// the email, password, and roles.
			if (email != null) {
				Security security = securityRepository.findByEmail(email);
				UserPrincipal userPrincipal = new UserPrincipal(security);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
						null, userPrincipal.getAuthorities());
				return authentication;
			}
			return null;
		}
		return null;
	}
}
