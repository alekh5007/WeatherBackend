package com.exam.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.helper.UserFoundException;
import com.exam.models.Role;
import com.exam.models.User;
import com.exam.models.UserRole;
import com.exam.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class Usercontroller {

	@Autowired
	private UserService userservice;
	
	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test")
	public String test() {
		System.out.println("++++++++++exam-server+++++++++++++");
		return"Welcome to back-end api";
	}
	
	@PostMapping("/")
	public User createuser(@RequestBody User user) throws Exception {
		System.out.println(user);
		user.setProfile("default.jpg");
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		
		Set<UserRole> role=new HashSet<UserRole>();
		UserRole userrole=new UserRole();
		Role rol=new Role();
		rol.setRoleid(45L);
		rol.setRoleName("NORMAL");
		
		
		
		userrole.setUser(user);
		userrole.setRole(rol);
		
		role.add(userrole);
		
		User createUser = this.userservice.createUser(user, role);
		return createUser;
	}
	
	//get user by username
	
	@GetMapping("/{username}")
	public User getUser(@PathVariable("username") String username) {
		User getuser = this.userservice.getuser(username);
		return  getuser;
	}
	
	//delete user by id
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable("userId") Long userId) {
		this.userservice.deleteuser(userId);
	}
	
	@ExceptionHandler(UserFoundException.class)
	public ResponseEntity<?>exceptionHandler(UserFoundException ex){
		  //response.put("error", "an error expected on processing file");
	        return ResponseEntity.badRequest().body("user with this username are already in databases!!!");
	}
}
