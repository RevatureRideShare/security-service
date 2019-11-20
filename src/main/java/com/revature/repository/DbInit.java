package com.revature.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.revature.bean.Security;

import java.util.Arrays;
import java.util.List;

//! The DbInit class initializes the database when the application is first started. 
//! Be careful as there is a method for demoing the service that truncates the security table on initial startup and provides it with a clean set of users.
@Service
public class DbInit implements CommandLineRunner {
	private SecurityRepository securityRepository;
	private PasswordEncoder passwordEncoder;

	public DbInit(SecurityRepository securityRepository, PasswordEncoder passwordEncoder) {
		this.securityRepository = securityRepository;
		this.passwordEncoder = passwordEncoder;
	}

	//! This method sets up the initial database state on every run of the Spring Boot application.
	//! COMMENT OUT OR DELETE THE METHOD WHEN THE FINAL VERSION OF THE APPLICATION IS IN USE.
	//! This method relies on the Spring Boot dependency.
	@Override
	public void run(String... args) {
		// Delete all the previous users in the database.
		this.securityRepository.deleteAll();

		// Create new encoded users.
		Security test = new Security("test@test.com", passwordEncoder.encode("pass"), "NULL");
		Security user = new Security("user@user.com", passwordEncoder.encode("pass"), "USER");
		Security admin = new Security("admin@admin.com", passwordEncoder.encode("pass"), "ADMIN");

		List<Security> users = Arrays.asList(test, user, admin);

		// Save the encoded users to the database.
		this.securityRepository.saveAll(users);
	}
}
