package com.exam.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.exam.config.JwtUtil;
import com.exam.models.JwtRequest;
import com.exam.models.JwtResponse;
import com.exam.models.User;
import com.exam.models.Weather;
import com.exam.service.impl.UserDetailServiceImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@RestController
@CrossOrigin("*")
public class AuthenticateController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailServiceImpl userDetailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	//generate token
	@PostMapping("/generate-token")
	public  Object  generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
		
		try {
			
			authenticate(jwtRequest.getUsername(),jwtRequest.getPassword());
			
		} catch (UsernameNotFoundException e) {
		
			throw new Exception("USER NOT FOUND"+e.getMessage());
		}
		
		UserDetails loadUserByUsername = this.userDetailService.loadUserByUsername(jwtRequest.getUsername());
		  String generateToken = this.jwtUtil.generateToken(loadUserByUsername);
		
		return new JwtResponse(generateToken);
	}
	
	
	
	public void authenticate(String username,String password) throws Exception {
		try {
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
		} catch (DisabledException e) {
			throw new Exception("User Disabled");
			
			
		} catch (BadCredentialsException e) {
			//throw new Exception("Invalid credentials"+e.getMessage());
			throw new ResponseStatusException(
			           HttpStatus.NOT_FOUND, "Foo Not Found");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//get current user
	@GetMapping("/current-user")
	public User getCurrentUser(Principal principal) {
		UserDetails loadUserByUsername = this.userDetailService.loadUserByUsername(principal.getName());
		return (User) loadUserByUsername;
	}
	
	@GetMapping("/covid")
	public String covid() {
	
		try {
			URL urlForGetRequest = new URL("https://api.covid19api.com/summary");
		    String readLine = null;
		    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
		    conection.setRequestMethod("GET");
		    conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
		    int responseCode = conection.getResponseCode();



		    if (responseCode == HttpURLConnection.HTTP_OK) {
		        BufferedReader in = new BufferedReader(
		            new InputStreamReader(conection.getInputStream()));
		        StringBuffer response = new StringBuffer();
		        while ((readLine = in .readLine()) != null) {
		            response.append(readLine);
		        } in .close();
		        // print result
		        System.out.println("JSON String Result " + response.toString());
		        return   response.toString();
		        //GetAndPost.POSTRequest(response.toString());
		    } else {
		        System.out.println("GET NOT WORKED");
		    }
		} catch (Exception e) {
	        e.printStackTrace();
		}
		return "yes";
		
	}
	
	
	@PostMapping("/weather")
	public String weather(@RequestBody Weather weather) {
		try {
			String c=weather.getCity();
			System.out.println(c);

		    final String POST_PARAMS = "{\n" + "\"userId\": 101,\r\n" +
		        "    \"id\": 101,\r\n" +
		        "    \"title\": \"Test Title\",\r\n" +
		        "    \"body\": \"Test Body\"" + "\n}";
		    System.out.println(POST_PARAMS);
		    URL obj = new URL("https://api.openweathermap.org/data/2.5/weather?q=+"+weather.getCity()+"&appid=936ac9dfb3fc5ac43059dc2148d0eccf");
		    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		    postConnection.setRequestMethod("POST");
		   // postConnection.setRequestProperty("userId", "a1bcdefgh");
		    postConnection.setRequestProperty("Content-Type", "application/json");


		    postConnection.setDoOutput(true);
		    OutputStream os = postConnection.getOutputStream();
		    os.write(POST_PARAMS.getBytes());
		    os.flush();
		    os.close();


		    int responseCode = postConnection.getResponseCode();
		    System.out.println("POST Response Code :  " + responseCode);
		    System.out.println("POST Response Message : " + postConnection.getResponseMessage());

		    if (responseCode == HttpURLConnection.HTTP_OK) { //success
		        BufferedReader in = new BufferedReader(new InputStreamReader(
		            postConnection.getInputStream()));
		        String inputLine;
		        StringBuffer response = new StringBuffer();

		        while ((inputLine = in .readLine()) != null) {
		            response.append(inputLine);
		        } in .close();

		        // print result
		        System.out.println(response.toString());
		        return response.toString();
		    } else {
		        System.out.println("POST NOT WORKED");
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

}
