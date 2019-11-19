package com.revature.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.repository.SecurityRepository;

// This controller does not have a login function held within it, because spring security autogenerates a login function mapped to /login (not /test/login).
@RestController
@RequestMapping("test")
@CrossOrigin
public class SecurityController {

	//TODO: Try commenting this out and see if that changes anything.
	private SecurityRepository securityRepository;

	public SecurityController(SecurityRepository securityRepository) {
		this.securityRepository = securityRepository;
	}

	// Method available to all.
	@GetMapping("public")
	public String testPublic() {
		return "This is public.";
	}

	// Method available to users.
	@GetMapping("user")
	public String testUser() {
		return "This is for users only.";
	}

	// Method available to admins.
	@GetMapping("admin")
	public String testAdmin() {
		return "This is for admins only.";
		// Return a response entity that alters the status code to 200 if we continue to
		// the method and 403 if they don't have the correct credentials.
	}

}
