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

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	// This method configures different request mappings and attaches permissions to
	// each method.
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
				.antMatchers(HttpMethod.POST, "/security").permitAll()
				
				// Admin microservice:
				.antMatchers(HttpMethod.DELETE, "/user/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/admin").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
				
				// User microservice:
					// User controller methods:
				.antMatchers(HttpMethod.POST, "/user").hasRole("USER")
				//TODO: This method should also allow the user who owns the account to perform it (email of logged in user should equal the email in the URL).
				.antMatchers(HttpMethod.PUT, "/user/*").hasRole("ADMIN")  
				.antMatchers(HttpMethod.PATCH, "/user/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/user?role={role}").hasRole("USER")
				.antMatchers(HttpMethod.GET, "/user").hasRole("USER")
				.antMatchers(HttpMethod.GET, "/user/{email}").hasRole("USER")
					// Car controller methods:
				.antMatchers(HttpMethod.POST, "/car").hasRole("USER")
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
		
		// Below commented out section allows users to access any method.
		/*
		 * .authorizeRequests() .antMatchers("/**").permitAll()
		 * .antMatchers(HttpMethod.POST, "/**").permitAll();
		 */
	}

	// ???
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

		return daoAuthenticationProvider;
	}

	// Sets the password encoder and decoder.
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
