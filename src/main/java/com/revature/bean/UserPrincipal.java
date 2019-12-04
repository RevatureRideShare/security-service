package com.revature.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The UserPrincipal class represents a logged in user within a Spring Security context.
 * 
 * @author Michael
 *
 */
public class UserPrincipal implements UserDetails {

  /**
   * This field represents the Security object that the logged in user is based off of.
   */
  private Security security;

  /**
   * This constructor allows Spring to perform constructor injection of any beans required for the
   * UserPrincipal to function.
   * 
   * @param security
   */
  public UserPrincipal(Security security) {
    this.security = security;
  }

  /**
   * Gets list of what a user's roles are, which determines which methods they can run.
   */
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
    return this.security.getPassword();
  }

  @Override
  public String getUsername() {
    // Email stands in for username.
    return this.security.getEmail();
  }

  /**
   * This method checks if the account is expired. Right now there is no way for an account to
   * expire.
   */
  @Override
  public boolean isAccountNonExpired() {
    // Might should do something else.
    return true;
  }

  /**
   * This method checks if the account has been locked. Right now there is no way for an account to
   * be locked by the security micro-service.
   */
  @Override
  public boolean isAccountNonLocked() {
    // Might should do something else.
    return true;
  }

  /**
   * This method checks to see if the credentials have expired. Right now there is no way for the
   * credentials to expire.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    // Might should do something else.
    return true;
  }

  /**
   * This method checks to see if the account is enabled. Right now there is no way for the account
   * to be disabled.
   */
  @Override
  public boolean isEnabled() {
    // Might should do something else.
    return true;
  }

}
