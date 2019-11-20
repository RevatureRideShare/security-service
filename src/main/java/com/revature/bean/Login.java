package com.revature.bean;

//! The login bean class represents a login attempt and is used as a blueprint to translate login 
//! attempts from JSON to security objects in the security microservice.
public class Login {
	//! This variable is a field representing the email that a user is attempting to log in with.
	private String email;
	//! This variable is a field representing the password that a user is attempting to log in with.
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
