package com.revature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * SecurityServiceApplication is a class that is designed to act as the container for the main
 * method for running the Spring Boot Application.
 * 
 * @author Michael
 *
 */
@SpringBootApplication
@EnableEurekaClient
public class SecurityServiceApplication {

  /**
   * The main method exists to hold the SpringApplication.run command that runs the Spring Boot
   * Application. It takes in an array of arguments as any main method of a Java program would. The
   * main method relies on the Spring Boot dependency.
   * 
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(SecurityServiceApplication.class, args);
  }

  /**
   * Allows CORS policy issues to work.
   * 
   * @return
   */
  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("PATCH");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("DELETE");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

}
