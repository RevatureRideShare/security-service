package com.revature.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.bean.Security;
import com.revature.repository.SecurityRepository;

//! The purpose of the SecurityService class is to act as the service layer for the security microservice.
@Service
public class SecurityService {

	//! PasswordEncoder is a spring security variable that is used to encrypt passwords for storage in the database 
	//! and to encrypt passwords during login attempts for comparison to persisted passwords.
	private PasswordEncoder passwordEncoder;
	
	private SecurityRepository securityRepository;

	public SecurityService(PasswordEncoder passwordEncoder, SecurityRepository securityRepository) {
		this.passwordEncoder = passwordEncoder;
		this.securityRepository = securityRepository;
	}
	
	//!
	public Security createSecurity(Security security) {
		Security encodedSecurity = new Security(security.getEmail(), passwordEncoder.encode(security.getPassword()), "USER");
		Security responseSecurity = securityRepository.save(encodedSecurity);
		return responseSecurity;
	}
}
