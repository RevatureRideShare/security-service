package com.revature.repository;

import com.revature.bean.Security;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The DbInit class initializes the database when the application is first started. Be careful as
 * there is a method for demoing the service that truncates the security table on initial startup
 * and provides it with a clean set of users.
 * 
 * @author Michael
 *
 */
@Service
public class DbInit implements CommandLineRunner {

  /**
   * This field is a SecurityRepository object that handles DAO operations for the security table.
   */
  private SecurityRepository securityRepository;

  /**
   * This field is a PasswordEncoder object that provides the encryption method for passwords stored
   * in the security table.
   */
  private PasswordEncoder passwordEncoder;

  public DbInit(SecurityRepository securityRepository, PasswordEncoder passwordEncoder) {
    this.securityRepository = securityRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * This method sets up the initial database state on every run of the Spring Boot application.
   * This resets everything in the database. COMMENT OUT OR DELETE THE METHOD WHEN THE FINAL VERSION
   * OF THE APPLICATION IS IN USE. This method relies on the Spring Boot dependency.
   */
  @Override
  public void run(String... args) {
    // Delete all the previous users in the database.
    this.securityRepository.deleteAll();

    // Create new encoded users.
    Security test = new Security("test@test.com", passwordEncoder.encode("pass"), "NULL");
    Security user = new Security("user@user.com", passwordEncoder.encode("pass"), "USER");
    Security admin = new Security("admin@admin.com", passwordEncoder.encode("pass"), "ADMIN");

    List<Security> users = Arrays.asList(test, user, admin);

    // Save the encoded users to the database.
    this.securityRepository.saveAll(users);
  }

}
