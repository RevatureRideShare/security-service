package com.revature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.bean.Security;

//! SecurityRepository is an interface that handles DAO functions to and 
//! from the security table by leveraging the Spring Data dependency.
@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {
	
	//! This method will find a security object in the security table by the email field.
	//! The method takes in an email string representing the user's email.
	//! The method returns a security object that it finds in the security table or null if the email matches nothing.
	//! The method relies on the Spring Data dependency.
	Security findByEmail(String email);
}
