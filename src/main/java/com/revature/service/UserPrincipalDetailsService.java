package com.revature.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.revature.bean.Security;
import com.revature.bean.UserPrincipal;
import com.revature.repository.SecurityRepository;

//! The UserPrincipalDetailsService class is a service class representing business logic around the UserPrincipal class. 
@Service
public class UserPrincipalDetailsService implements UserDetailsService {
	private SecurityRepository securityRepository;

	public UserPrincipalDetailsService(SecurityRepository securityRepository) {
		this.securityRepository = securityRepository;
	}

	//! This method loads a user by email not by the username, but the method is named UserDetailsService
	//! because it overrides a method provided by Spring Security.
	//! The method takes in a String that represents the user's email.
	//! The method returns a UserPrincipal object that represents the logged in user.
	//! This method relies on the Spring Security dependency.
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		Security security = this.securityRepository.findByEmail(s);
		UserPrincipal userPrincipal = new UserPrincipal(security);

		return userPrincipal;
	}
}