package com.revature.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Security bean class represents a registered user that will be persisted in the database.
 * 
 * @author Michael
 *
 */
@Entity
@Table(name = "security_table")
public class Security {

  //! This field is the primary key for Security objects persisted in the security table.
  //! UUID did not work for userID because hibernate could not convert big decimal
  //! into UUID.
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  int userID;

  //! This field is the user's email address. It must be unique.
  @Column(name = "email")
  String email;

  //! This field is the user's password. It will be encrypted by the time the Security object is
  //! persisted in the security table.
  @Column(name = "password")
  String password;

  //! This field contains any Security roles the user has. Separate roles must be separated by ",
  //! ".
  @Column(name = "roles")
  private String roles = "";

  public String getRoles() {
    return roles;
  }

  public int getUserID() {
    return userID;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  /**
   * Get the roles string field as a list of strings. Gets list of roles for Spring Security to
   * authorize method calls off of.
   * 
   * @return
   */
  //! Get the roles string field as a list of strings.
  //! Gets list of roles for Spring Security to authorize method calls off of.
  public List<String> getRoleList() {
    if (this.roles.length() > 0) {
      return Arrays.asList(this.roles.split(", "));
    }
    return new ArrayList<>();
  }

  /**
   * This constructor is for adding new users where the role is the default ("USER").
   * 
   * @param userID Primary key of the user object in the table. Can be zero.
   * @param email Email of the user.
   * @param password Password of the user.
   */
  public Security(int userID, String email, String password) {
    super();
    this.userID = userID;
    this.email = email;
    this.password = password;
  }

  /**
   * This constructor is used by the SecurityService class for registering users with encrypted
   * passwords.
   * 
   * @param email email of user
   * @param password encrypted password of user
   * @param roles security roles of the user
   */
  //! This constructor is used by the SecurityService class for registering users with encrypted
  // passwords.
  public Security(String email, String password, String roles) {
    this.email = email;
    this.password = password;
    this.roles = roles;
  }

  //! This constructor is the no arguments constructor.
  public Security() {
    super();
  }

  @Override
  public String toString() {
    return "Security [userID=" + userID + ", email=" + email + ", password=" + password + ", roles="
        + roles + "]";
  }

}
