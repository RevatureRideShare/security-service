package com.revature.bean;

public class RegisterDto {

  private UserDto userDto;
  private String password;

  public UserDto getUserDto() {
    return userDto;
  }

  public void setUserDto(UserDto userDto) {
    this.userDto = userDto;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((userDto == null) ? 0 : userDto.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    RegisterDto other = (RegisterDto) obj;
    if (password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!password.equals(other.password)) {
      return false;
    }
    if (userDto == null) {
      if (other.userDto != null) {
        return false;
      }
    } else if (!userDto.equals(other.userDto)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RegisterDto [userDto=" + userDto + ", password=" + password + "]";
  }

  /**
   * Register data transfer object that is used to translate between angular and Java objects.
   * 
   * @param userDto Represents a user object.
   * @param password User's password.
   */
  public RegisterDto(UserDto userDto, String password) {
    super();
    this.userDto = userDto;
    this.password = password;
  }

  public RegisterDto() {
    super();
  }

}
