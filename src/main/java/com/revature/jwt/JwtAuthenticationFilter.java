package com.revature.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.bean.Login;
import com.revature.bean.UserPrincipal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The JwtAuthenticationFilter class uses the automatically generated /login mapping to authenticate
 * the user when they login. JwtAuthenticationFilter extends the
 * UsernamePasswordAuthenticationFilter class that exists in Spring Security.
 * 
 * @author Michael
 *
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  //! This field is an AuthenticationManager object provided by Spring Security.
  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  //! This method attempts to login a user.
  //! It takes in an HttpServletRequest and HttpServletResponse provided by the DispatcherServlet.
  //! The HttpServletRequest must contain a JSON object in the request body containing the email
  //! and the password.
  //! The method returns an Authentication object which contains a JWT.
  //! This method relies on the Spring Security dependency.
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    // Maps login request to login bean.
    Login login = null;

    try {
      login = new ObjectMapper().readValue(request.getInputStream(), Login.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Creates the login authentication token.
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword(),
            new ArrayList<>());

    // Authenticate the user.
    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    return authentication;
  }

  //! This method attaches JWT to response header if login is successful.
  //! The method takes in a HttpServletRequest and HttpServletResponse provided by Spring Security
  //! from the attemptAuthentication method.
  //! The method also takes in a FilterChain object and an Authentication object provided by Spring
  //! Security.
  //! The method does not return anything, but modifies the response referenced in
  //! attemptAuthentication based on whether authentication was successful or not.
  //! This method relies on the Spring Security dependency.
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    String host = "localhost";
    String port = "8090";

    // Getting currently logged in user.
    UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();

    // Opening new HTTP Request to the user service to have it get the correct user.
    URL obj;
    obj = new URL("HTTP://" + host + ":" + port + "/user/" + userPrincipal.getUsername());
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod(HttpMethod.GET);

    // Getting response.
    int responseCode = con.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      // If the response code is an "OK".
      // Print the response. 
      System.out.println("User response was Ok.");

      // Add response body of the user response to the security response.
      BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
      StringBuilder sb = new StringBuilder();
      String output;
      while ((output = br.readLine()) != null) {
        System.out.println(output);
        sb.append(output);
      }
      PrintWriter bodyWriter = response.getWriter();
      bodyWriter.write(sb.toString());
      bodyWriter.flush();

    } else {
      // If the response was not an "OK", print the response code and tell the user.
      System.out.println("Request did not work. Status Code: " + responseCode);
      response.setStatus(responseCode);
    }

    // Creating JWT token.
    String token = JWT.create().withSubject(userPrincipal.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
        .sign(HMAC512(JwtProperties.SECRET.getBytes()));

    // Adding JWT token in the response header.
    response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
  }

}
