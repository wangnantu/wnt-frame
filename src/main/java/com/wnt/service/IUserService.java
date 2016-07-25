package com.wnt.service;

import java.util.List;

import com.wnt.domain.User;

public interface IUserService {
	
	 public User getUserById(String userid);
	 
	 public int updateUser(User user);
	 
	 public List<User> getAllUsers();
}
