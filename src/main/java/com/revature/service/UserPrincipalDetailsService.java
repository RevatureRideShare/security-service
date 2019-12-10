package com.revature.service;

import static com.revature.util.LoggerUtil.trace;

import com.revature.bean.Security;
import com.revature.bean.UserPrincipal;
import com.revature.repository.SecurityRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The UserPrincipalDetailsService class is a service class representing business logic around the
 * UserPrincipal class.
 * 
 * @author Michael
 *
 */
@Service
public class UserPrincipalDetailsService implements UserDetailsService {

  /**
   * The SecurityRepository object performs DAO operations for the security table.
   */
  private SecurityRepository securityRepository;

  public UserPrincipalDetailsService(SecurityRepository securityRepository) {
    this.securityRepository = securityRepository;
  }

  /**
   * This method loads a user by email not by the username, but the method is named
   * UserDetailsService because it overrides a method provided by Spring Security. The method takes
   * in a String that represents the user's email. The method returns a UserPrincipal object that
   * represents the logged in user. This method relies on the Spring Security dependency.
   */
  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    trace("Inside loadUserbyUsername method.");
    Security security = this.securityRepository.findByEmail(s);
    trace("Using findByEmail repo method.");
    UserPrincipal userPrincipal = new UserPrincipal(security);
    trace("Returning userPrincipal");
    return userPrincipal;
  }

}
