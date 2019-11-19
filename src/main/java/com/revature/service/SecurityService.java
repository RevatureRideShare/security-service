package com.revature.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.bean.Security;
import com.revature.repository.SecurityRepository;

public class SecurityService {

	@Autowired
	private SecurityRepository securityRepository;

	public void setSecurityRepository(SecurityRepository securityRepository) {
		this.securityRepository = securityRepository;
	}

	public Security login(String email) {
		Security security = securityRepository.findByEmail(email);
		return security;
	}
}
