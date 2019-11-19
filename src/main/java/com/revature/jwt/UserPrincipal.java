package com.revature.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.revature.bean.Security;

public class UserPrincipal implements UserDetails {
	private Security security;

	public UserPrincipal(Security security) {
		this.security = security;
	}

	// Gets list of what a user's roles are, which determines which methods they can
	// run.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();

		// Extract list of roles (ROLE_name)
		this.security.getRoleList().forEach(r -> {
			GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + r);
			authorities.add(authority);
		});

		return authorities;
	}

	@Override
	public String getPassword() {
		System.out.println(this.security);
		return this.security.getPassword();
	}

	@Override
	public String getUsername() {
		// Email stands in for username.
		return this.security.getEmail();
	}

	// Checks if the account is expired.
	@Override
	public boolean isAccountNonExpired() {
		// Might should do something else.
		return true;
	}

	// Checks if the account has been locked.
	@Override
	public boolean isAccountNonLocked() {
		// Might should do something else.
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Might should do something else.
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Might should do something else.
		return true;
	}
}
