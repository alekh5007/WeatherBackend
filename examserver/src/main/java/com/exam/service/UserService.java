package com.exam.service;

import java.util.Set;

import com.exam.models.User;
import com.exam.models.UserRole;

public interface UserService {
	
	public User createUser(User user,Set<UserRole> userRoles) throws Exception;
	
	public User getuser(String username);

	public void deleteuser(Long userId);
}
