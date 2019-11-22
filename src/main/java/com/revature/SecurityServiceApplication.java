package com.revature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SecurityServiceApplication is a class that is designed to act as the container for the main
 * method for running the Spring Boot Application.
 * 
 * @author Michael
 *
 */
@SpringBootApplication
public class SecurityServiceApplication {

  //! The main method exists to hold the SpringApplication.run command that runs the Spring Boot
  //! Application.
  //! It takes in an array of arguments as any main method of a Java program would.
  //! The main method relies on the Spring Boot dependency.
  public static void main(String[] args) {
    SpringApplication.run(SecurityServiceApplication.class, args);
  }

}
