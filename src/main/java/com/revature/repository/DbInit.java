package com.revature.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.revature.bean.Security;

import java.util.Arrays;
import java.util.List;

// This class initializes the database when the application is first started.
@Service
public class DbInit implements CommandLineRunner {
	private SecurityRepository securityRepository;
	private PasswordEncoder passwordEncoder;

	public DbInit(SecurityRepository securityRepository, PasswordEncoder passwordEncoder) {
		this.securityRepository = securityRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// Sets up the initial database state.
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
