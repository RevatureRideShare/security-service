package com.revature.controller;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.bean.RegisterDto;
import com.revature.bean.Security;
import com.revature.jwt.JwtProperties;
import com.revature.repository.SecurityRepository;
import com.revature.service.SecurityService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.ws.rs.HttpMethod;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The SecurityController is a controller that acts as communication between the DispatcherServlet
 * and the rest of the program. SecurityController contains all of the endpoints accessible within
 * the entire microservice ecosystem. Note that SecurityController does not have a login function
 * held within it, because spring security automatically generates the login functionality mapped to
 * /login.
 * 
 * @author Michael
 *
 */
@RestController
@RequestMapping("/")
@CrossOrigin
public class SecurityController {

  //! This field is the SecurityRepository object that handles DAO operations in the security
  //! table.
  private SecurityRepository securityRepository;

  //! This field is the SecurityService object that handles business logic for the security
  //! microservice.
  private SecurityService securityService;

  //! This constructor allows Spring to perform constructor injection of any required beans for the
  //! SecurityController class to operate.
  public SecurityController(SecurityRepository securityRepository,
      SecurityService securityService) {
    this.securityRepository = securityRepository;
    this.securityService = securityService;
  }

  //! Test method available to anyone. Helpful for Postman testing.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("test/public")
  public String testPublic() {
    return "Success";
  }

  //! Test method available to users. Helpful for Postman testing.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("test/user")
  public String testUser() {
    return "Success";
  }

  //! Test method available to admins. Helpful for Postman testing.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("test/admin")
  public String testAdmin() {
    return "Success";
  }

  // Security microservice:
  /**
   * Method for directing post requests for persisting new security objects in the security table.
   * Primarily for testing purposes. Takes a JSON object from the request body and transforms it
   * into a security object. Returns a response with the 200 status code if the operation was
   * successful, or a 400 status code if the operation failed (almost certainly due to a duplicate
   * email).
   * 
   * @param security Security object passed into the endpoint when a test is performed.
   * @return
   */
  @PostMapping("/security")
  public ResponseEntity createSecurity(@RequestBody Security security) {
    try {
      securityService.createNullSecurity(security);
      return new ResponseEntity(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }

  // Hystrix-Dashboard microservice:
  @GetMapping("/actuator/hystrix.stream")
  public String getHystrix() {
    return "Success";
  }

  // Admin microservice:
  //! Method for authorizing deleting users from the user table in the user microservice.
  //! Security objects in the security table are never deleted for record keeping purposes.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @DeleteMapping("/user/*")
  public String deleteUser() {
    return "Success";
  }

  /**
   * Method for authorizing creating new admins in the admin table in the admin microservice.
   * Requires a JWT passed in the HttpRequest header. Returns a 200 status code with "Success" in
   * the response body if the user is allowed to perform the operation, returns a 403 status code if
   * the user isn't allowed to perform the operation.
   * 
   * @return Returns a response entity with a status code based on whether the operation was
   *         successful or not.
   */
  @PostMapping("/admin")
  public ResponseEntity createAdmin(@RequestBody RegisterDto registerDto) {
    System.out.println(registerDto);
    String host = "localhost";
    String port = "8090";
    try {
      Security security =
          new Security(registerDto.getUserDto().getEmail(), registerDto.getPassword());
      securityService.createAdminSecurity(security);
      // Opening new HTTP Request to the admin service to have it create a new admin.
      URL obj;
      obj = new URL("HTTP://" + host + ":" + port + "/user");
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod(HttpMethod.POST);
      int responseCode = con.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        // If the response code is an "OK".
        // Print the response. 
        System.out.println("User response was Ok.");
        return new ResponseEntity(HttpStatus.OK);
      } else {
        // If the response was not an "OK", print the response code and tell the user.
        System.out.println("Request did not work. Status Code: " + responseCode);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }

  //! Method for authorizing getting a list of all admins in the admin table in the admin
  //! microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("/admin")
  public String getAllAdmins() {
    return "Success";
  }

  // User microservice:
  // User controller methods:
  /**
   * Method for authorizing creation of new users in the user table in the user microservice.
   * Returns a 200 status code with "Success" in the response body if the user is allowed to perform
   * the operation, returns a 403 status code if the user isn't allowed to perform the operation.
   * 
   * @return Returns a response entity with a status code based on whether the operation was
   *         successful or not.
   */
  @PostMapping("/user")
  public ResponseEntity createUser(@RequestBody RegisterDto registerDto) {
    System.out.println(registerDto);
    String host = "localhost";
    String port = "8090";
    try {
      // Creating new security object in the database.
      Security security =
          new Security(registerDto.getUserDto().getEmail(), registerDto.getPassword());
      securityService.createUserSecurity(security);

      // Creating the new JWT.
      String token = JWT.create().withSubject(registerDto.getUserDto().getEmail())
          .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
          .sign(HMAC512(JwtProperties.SECRET.getBytes()));

      // Adding JWT token in the response header.
      HttpHeaders headers = new HttpHeaders();
      headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);

      // Opening new HTTP Request to the user service to have it create a new user.
      URL obj;
      obj = new URL("HTTP://" + host + ":" + port + "/user");
      System.out.println("HTTP://" + host + ":" + port + "/user");
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod(HttpMethod.POST);

      // Turning UserDto into JSON.
      ObjectMapper om = new ObjectMapper();
      String userDtoOut = om.writeValueAsString(registerDto.getUserDto());
      System.out.println(userDtoOut);

      // Attach the correct body to the request.
      con.setDoOutput(true);
      con.setRequestProperty("Content-Type", "application/json; utf-8");

      // Sending HTTP Request.
      OutputStream os = con.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
      osw.write(userDtoOut);
      System.out.println("Wrote JSON to request body.");
      osw.flush();
      osw.close();
      os.close();
      System.out.println("Closed streams.");

      // Reading response.
      int responseCode = con.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_CREATED) {
        // If the response code is an "OK".
        // Print the response code. 
        System.out.println("Request was successful. Status Code: " + responseCode + ".");

        // Get and print the response body.
        BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
          sb.append(output);
        }
        System.out.println(sb);

        // Attach the response body to the response entity.
        return new ResponseEntity(sb, headers, HttpStatus.CREATED);
      } else {
        // If the response was not an "OK", print the response code and tell the user.
        System.out.println("Request did not work. Status Code: " + responseCode);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
      }

    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }

  //! Method for authorizing updates to entire user object in the user table in the user
  //! microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @PutMapping("/user/{email}")
  public String updateUser() {
    return "Success";
  }

  //! Method for authorizing partial updates to any user object in the user table in the user
  //! microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @PatchMapping("/user/*")
  public String patchUser() {
    return "Success";
  }

  //! Method for authorizing getting all users who have a specific role from the user table in the
  //! user microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("/user?role={role}")
  public String getUsersByRole() {
    return "Success";
  }

  //! Method for authorizing getting all users from the user table in the user microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("/user")
  public String getAllUsers() {
    return "Success";
  }

  //! Method for authorizing getting a single user from the user table by their email in the user
  //! microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("/user/{email}")
  public String getUserByEmail() {
    return "Success";
  }

  // Car controller methods:
  //! Method for authorizing creation of a new car in the car table in the user microservice.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @PostMapping("/car")
  public String createCar() {
    return "Success";
  }

  //! Method for authorizing getting the cars owned by a specific user based on the user's email in
  //! the user microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("/user/{email}/car")
  public String getCarsByUser() {
    return "Success";
  }

  // Location microservice:
  // Housing location controller methods:
  //! Method for authorizing creation of a new housing location from the housing-location table in
  //! the location microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @PostMapping("/housing-location")
  public String createHousingLocation() {
    return "Success";
  }

  //! Method for authorizing getting a list of all housing locations from the housing-location
  //! table in the location microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("/housing-location")
  public String getAllHousingLocations() {
    return "Success";
  }

  //! Method for authorizing getting a list of all housing locations by the training location from
  //! the housing location table in the location microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("/housing-location/{training-location}/housing-location")
  public String getHousingLocationsByTrainingLocation() {
    return "Success";
  }

  // Training location controller methods:
  //! Method for authorizing creation of new training locations in the training-location table in
  //! the location microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @PostMapping("/training-location")
  public String createTrainingLocation() {
    return "Success";
  }

  //! Method for authorizing getting a list of all training locations from the training-location
  //! table in the location microservice.
  //! Requires a JWT passed in the HttpRequest header.
  //! Returns a 200 status code with "Success" in the response body if the user is allowed to
  //! perform the operation,
  //! returns a 403 status code if the user isn't allowed to perform the operation.
  @GetMapping("/training-location")
  public String getAllTrainingLocations() {
    return "Success";
  }

}
