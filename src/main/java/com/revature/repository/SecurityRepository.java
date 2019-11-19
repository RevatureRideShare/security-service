package com.revature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.bean.Security;

// Repository that handles DAO functions to and from security table.
@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {
	Security findByEmail(String email);
}
