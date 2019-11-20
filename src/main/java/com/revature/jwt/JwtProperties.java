package com.revature.jwt;

//! JwtProperties is the class that acts as the basis to build the JWT that is sent back on user login.
public class JwtProperties {
	
	//! This field is a string that helps verify that JWTs provided by the client came from the security microservice.
	public static final String SECRET = "Batch1909JavaFullStackIsTheGreatest";
	
	// public static final int EXPIRATION_TIME = 864_000_000; // 10 Days
	//! This field is a integer that represents the time until the JWT expires in milliseconds.
	public static final int EXPIRATION_TIME = 259_200_000; // 3 Days before token expires.
	
	//! This field is a prefix attached to the value of the JWT key value pair. It is provided in plain text.
	public static final String TOKEN_PREFIX = "Bearer ";
	
	//! This field is the key in the key value pair provided in the JWT.
	public static final String HEADER_STRING = "Authorization";
	
}
