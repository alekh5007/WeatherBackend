package com.exam.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.exam.helper.UserFoundException;
import com.exam.models.User;
import com.exam.models.UserRole;
import com.exam.repo.RoleRepo;
import com.exam.repo.UserRepo;
import com.exam.service.UserService;

@Component
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	
	//creating user
	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception   {
		
		User save = this.userRepo.findByUsername(user.getUsername());
		if(save!=null) {
			//throw new Exception("user already exist");
			throw new UserFoundException("user already exist with this username");
			//System.out.println("user already exist");
			
		}else {
			for(UserRole ur:userRoles) {
				roleRepo.save(ur.getRole());
			}
			//Set<UserRole> userRole = user.getUserRole();			
			user.getUserRole().addAll(userRoles);
			 save = this.userRepo.save(user);
		}
		return save;
		
	}


	@Override
	public User getuser(String username) {
		User findByUsername = this.userRepo.findByUsername(username);
		return findByUsername;
	}


	@Override
	public void deleteuser(Long userId) {
		this.userRepo.deleteById(userId);
		
	}

}
