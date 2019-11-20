package com.revature.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.revature.repository.SecurityRepository;
import com.revature.service.UserPrincipalDetailsService;
import com.revature.jwt.JwtAuthenticationFilter;
import com.revature.jwt.JwtAuthorizationFilter;

//! The SecurityConfiguration class performs initial setup for the security microservice 
//! when it is first started.
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private UserPrincipalDetailsService userPrincipalDetailsService;
	private SecurityRepository securityRepository;

	public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService,
			SecurityRepository securityRepository) {
		this.userPrincipalDetailsService = userPrincipalDetailsService;
		this.securityRepository = securityRepository;
	}

	//! This configure method sets the AuthenticationManagerBuilder object that will be used by Spring Security.
	//! This method takes in an AuthenticationManagerBuilder object provided by Spring Security.
	//! This method relies on the Spring Security dependency.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	//! This configure method configures the roles required to perform any requests given to the microservice ecosystem.
	//! The method runs on initial startup of the Spring Boot Application.
	//! The method takes in a HttpSecurity object provided by Spring Security.
	//! The method relies on the Spring Security dependency.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// Removing the CSRF and state in the current session because when we use JWT we
				// do not need them.
				.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				
				// Adding the JWT authentication and authorization filters.
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), this.securityRepository))
				.authorizeRequests()
				
				// Configuring the access rules for each test endpoint.
				.antMatchers(HttpMethod.GET, "/test/public").permitAll()
				.antMatchers("/test/user").hasAnyRole("USER", "ADMIN")
				.antMatchers("/test/admin/").hasRole("ADMIN")
				
				// Configuring the access rules for each real endpoint.
				
				// Security microservice:
				.antMatchers(HttpMethod.POST, "/security").permitAll() // User can't have a role because they are creating a new user account.
				
				// Admin microservice:
				.antMatchers(HttpMethod.DELETE, "/user/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/admin").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
				
				// User microservice:
					// User controller methods:
				.antMatchers(HttpMethod.POST, "/user").permitAll() // User can't have a role because they are creating a new user account.
				//TODO: This method should also allow the user who owns the account to perform it (email of logged in user should equal the email in the URL).
				.antMatchers(HttpMethod.PUT, "/user/*").hasRole("ADMIN")  
				.antMatchers(HttpMethod.PATCH, "/user/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/user?role={role}").hasRole("USER")
				.antMatchers(HttpMethod.GET, "/user").hasRole("USER")
				.antMatchers(HttpMethod.GET, "/user/{email}").hasRole("USER")
					// Car controller methods:
				.antMatchers(HttpMethod.POST, "/car").permitAll() // User can't have a role because they are creating a new user account.
				.antMatchers(HttpMethod.GET, "/user/{email}/car").hasRole("USER")
				
				// Location microservice:
					// Housing location controller methods:
				.antMatchers(HttpMethod.POST, "/housing-location").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/housing-location").hasRole("USER")
				.antMatchers(HttpMethod.GET, "/housing-location/{training-location}/housing-location").hasRole("USER")
					// Training location controller methods:
				.antMatchers(HttpMethod.POST, "/training-location").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/training-location").hasRole("USER")
				
				// End of statement.
				.anyRequest()
				.authenticated();
		
	}

	//! This method provides the DaoAuthenticationProvider object required by Spring Security to ensure that users are authenticated correctly.
	//! The method also sets the passwordEncoder to be used for the entire application.
	//! This method relies on the Spring Security dependency.
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

		return daoAuthenticationProvider;
	}

	//! This method allows Spring to create and manage the passwordEncoder bean.
	//! This method relies on the Spring Core dependency.
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
