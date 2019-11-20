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

	// Loads user by email not by the username, but the method in UserDetailsService
	// is named loadUserByUsername so it had to remain the same.
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		Security security = this.securityRepository.findByEmail(s);
		UserPrincipal userPrincipal = new UserPrincipal(security);

		return userPrincipal;
	}
}