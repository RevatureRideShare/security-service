package com.revature.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.bean.Security;
import com.revature.repository.SecurityRepository;
import com.revature.service.SecurityService;

//! The SecurityController is a controller that acts as communication between the DispatcherServlet and the rest of the program.
//! SecurityController contains all of the endpoints accessible within the entire microservice ecosystem.
//! Note that SecurityController does not have a login function held within it, because spring security 
//! automatically generates the login functionality mapped to /login.
@RestController
@RequestMapping("/")
@CrossOrigin
public class SecurityController {

	private SecurityRepository securityRepository;
	private SecurityService securityService;
	
	public SecurityController(SecurityRepository securityRepository, SecurityService securityService) {
		this.securityRepository = securityRepository;
		this.securityService = securityService;
	}
	
	// Test method available to all.
	@GetMapping("test/public")
	public String testPublic() {
		return "Success";
	}

	// Test method available to users.
	@GetMapping("test/user")
	public String testUser() {
		return "Success";
	}

	// Test method available to admins.
	@GetMapping("test/admin")
	public String testAdmin() {
		return "Success";
		// Return a response entity that alters the status code to 200 if we continue to
		// the method and 403 if they don't have the correct credentials.
	}
	
	// Security microservice:
	@PostMapping("/security")
	public ResponseEntity createSecurity(@RequestBody Security security) {
		try {
			securityService.createSecurity(security);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	// Admin microservice:
	@DeleteMapping("/user/*")
	public String deleteUser() {
		return "Success";
	}
	
	@PostMapping("/admin")
	public String createAdmin() {
		return "Success";
	}
	
	@GetMapping("/admin")
	public String getAllAdmins() {
		return "Success";
	}
	
	// User microservice:
		// User controller methods:
	@PostMapping("/user")
	public String createUser() {
		return "Success";
	}
	
	@PutMapping("/user/*")
	public String updateUser() {
		return "Success";
	}
	
	@PatchMapping("/user/*")
	public String patchUser() {
		return "Success";
	}
	
	@GetMapping("/user?role={role}")
	public String getUsersByRole() {
		return "Success";
	}
	
	@GetMapping("/user")
	public String getAllUsers() {
		return "Success";
	}
	
	@GetMapping("/user/{email}")
	public String getUsersByEmail() {
		return "Success";
	}
	
		// Car controller methods:
	@PostMapping("/car")
	public String createCar() {
		return "Success";
	}
	
	@GetMapping("/user/{email}/car")
	public String getCarsByUser() {
		return "Success";
	}
	
	// Location microservice:
		// Housing location controller methods:
	@PostMapping("/housing-location")
	public String createHousingLocation() {
		return "Success";
	}
	
	@GetMapping("/housing-location")
	public String getAllHousingLocations() {
		return "Success";
	}
	
	@GetMapping("/housing-location/{training-location}/housing-location")
	public String getHousingLocationsByTrainingLocation() {
		return "Success";
	}
	
		// Training location controller methods:
	@PostMapping("/training-location")
	public String createTrainingLocation() {
		return "Success";
	}
	
	@GetMapping("/training-location")
	public String getAllTrainingLocations() {
		return "Success";
	}
}
