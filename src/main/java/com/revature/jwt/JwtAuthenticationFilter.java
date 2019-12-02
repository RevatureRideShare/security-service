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
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

  private static Logger log = Logger.getLogger("JwtAuthenticationFilter");

  //! This field is an AuthenticationManager object provided by Spring Security.
  private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  /**
   * This method attempts to login a user. It takes in an HttpServletRequest and HttpServletResponse
   * provided by the DispatcherServlet. The HttpServletRequest must contain a JSON object in the
   * request body containing the email and the password. The method returns an Authentication object
   * which contains a JWT. This method relies on the Spring Security dependency.
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    log.info(
        "Inside JwtAuthenticationFilter's attemptAuthentication method, with HttpServletRequest "
            + request.toString() + ", HttpServletRsponse " + response.toString());
    // Maps login request to login bean.
    Login login = null;

    try {
      log.info("Trying to create the login object");
      login = new ObjectMapper().readValue(request.getInputStream(), Login.class);
    } catch (IOException e) {
      log.info("Catching exception " + e.getMessage());
      e.printStackTrace();
    }

    // Creates the login authentication token.
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword(),
            new ArrayList<>());

    log.info("Created UsernamePasswordAuthenticationToken " + authenticationToken.toString());

    // Authenticate the user.
    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    log.info("Created Authentication " + authentication.toString());

    log.info("Returning Authentication " + authentication.toString());
    return authentication;
  }

  /**
   * This method attaches JWT to response header if login is successful. The method takes in a
   * HttpServletRequest and HttpServletResponse provided by Spring Security from the
   * attemptAuthentication method. The method also takes in a FilterChain object and an
   * Authentication object provided by Spring Security. The method does not return anything, but
   * modifies the response referenced in attemptAuthentication based on whether authentication was
   * successful or not. This method relies on the Spring Security dependency.
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    log.info("Inside method successfulAuthentication");

    String host = "localhost";
    String userPort = "8090";
    String adminPort = "8091";

    // Getting currently logged in user.
    UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();

    log.info("Created userPrincipal " + userPrincipal.toString());

    Set<String> roles =
        authResult.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
    log.info("Created set of roles " + roles.toString());

    if (roles.contains("ROLE_ADMIN")) {
      log.info("Set of roles contains ROLE_ADMIN");

      try {
        // Opening new HTTP Request to the user service to have it get the correct user.
        URL obj;
        obj = new URL("HTTP://" + host + ":" + adminPort + "/admin/" + userPrincipal.getUsername());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(HttpMethod.GET);

        log.info("Set request method of GET /admin/" + userPrincipal.toString());

        // Getting response.
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          log.info("ResponseCode is HttpURLConnection.HTTP_OK");
          // If the response code is an "OK".

          // Creating JWT token.
          String token = JWT.create().withSubject(userPrincipal.getUsername())
              .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
              .sign(HMAC512(JwtProperties.SECRET.getBytes()));

          log.info("Created JWT token " + token);

          // Adding JWT token in the response header.
          response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
          log.info("Added JWT token in response header");

          // Add response body of the user response to the security response.
          BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
          StringBuilder sb = new StringBuilder();
          String output;
          while ((output = br.readLine()) != null) {
            sb.append(output);
          }
          PrintWriter bodyWriter = response.getWriter();
          bodyWriter.write(sb.toString());
          bodyWriter.flush();

        } else {
          // If the response was not an "OK" set the response code.
          response.setStatus(responseCode);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (roles.contains("ROLE_USER")) {

      try {
        // Opening new HTTP Request to the user service to have it get the correct user.
        URL obj;
        obj = new URL("HTTP://" + host + ":" + userPort + "/user/" + userPrincipal.getUsername());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(HttpMethod.GET);

        // Getting response.
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          // If the response code is an "OK".

          // Creating JWT token.
          String token = JWT.create().withSubject(userPrincipal.getUsername())
              .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
              .sign(HMAC512(JwtProperties.SECRET.getBytes()));

          // Adding JWT token in the response header.
          response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);

          // Add response body of the user response to the security response.
          BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
          StringBuilder sb = new StringBuilder();
          String output;
          while ((output = br.readLine()) != null) {
            sb.append(output);
          }
          PrintWriter bodyWriter = response.getWriter();
          bodyWriter.write(sb.toString());
          bodyWriter.flush();

        } else {
          // If the response was not an "OK", print the response code and tell the user.
          response.setStatus(responseCode);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("User has no roles.");
    }

  }

}
