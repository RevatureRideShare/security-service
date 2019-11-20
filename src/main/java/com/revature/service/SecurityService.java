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
	
	//! The SecurityRepository object peforms DAO operations for the security table.
	private SecurityRepository securityRepository;

	public SecurityService(PasswordEncoder passwordEncoder, SecurityRepository securityRepository) {
		this.passwordEncoder = passwordEncoder;
		this.securityRepository = securityRepository;
	}
	
	//! The createUserSecurity method is used when registering a new user. 
	//! It creates the Security object that gets persisted in the security table.
	//! The method takes in a Security object that should include the email and password of the user.
	//! All Security objects created with this method have the "USER" role by default.
	//! The method returns a Security object which is the Security that was persisted in the database.
	public Security createUserSecurity(Security security) {
		Security encodedSecurity = new Security(security.getEmail(), passwordEncoder.encode(security.getPassword()), "USER");
		Security responseSecurity = securityRepository.save(encodedSecurity);
		return responseSecurity;
	}
	

	//! The createAdminSecurity method is used when registering a new admin. 
	//! It creates the Security object that gets persisted in the security table.
	//! The method takes in a Security object that should include the email and password of the user.
	//! All Security objects created with this method have the "ADMIN" role by default.
	//! The method returns a Security object which is the Security that was persisted in the database.
	public Security createAdminSecurity(Security security) {
		Security encodedSecurity = new Security(security.getEmail(), passwordEncoder.encode(security.getPassword()), "ADMIN");
		Security responseSecurity = securityRepository.save(encodedSecurity);
		return responseSecurity;
	}
	
	//! The createNullSecurity method is used when registering a new Security object for testing purposes. 
	//! It creates the Security object that gets persisted in the security table.
	//! The method takes in a Security object that should include the email and password of the user.
	//! All Security objects created with this method have the "NULL" role by default.
	//! The method returns a Security object which is the Security that was persisted in the database.
	public Security createNullSecurity(Security security) {
		Security encodedSecurity = new Security(security.getEmail(), passwordEncoder.encode(security.getPassword()), "NULL");
		Security responseSecurity = securityRepository.save(encodedSecurity);
		return responseSecurity;
	}

}
