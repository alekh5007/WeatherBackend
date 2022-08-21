package com.exam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.exam.models.User;
import com.exam.repo.UserRepo;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userrepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User findByUsername = this.userrepo.findByUsername(username);
		if(findByUsername==null) {
			System.out.println("user not found");
			throw new UsernameNotFoundException("no user found");
		}
		return findByUsername;
	}

}
