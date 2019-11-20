package com.revature.jwt;

//! JwtProperties is the class that acts as the basis to build the JWT that is sent back on user login.
public class JwtProperties {
	public static final String SECRET = "Batch1909JavaFullStackIsTheGreatest";
	// public static final int EXPIRATION_TIME = 864_000_000; // 10 Days
	public static final int EXPIRATION_TIME = 259_200_000; // 3 Days before token expires.
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
}
