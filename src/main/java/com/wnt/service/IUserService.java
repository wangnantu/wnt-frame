package com.wnt.service;

import com.wnt.domain.User;

public interface IUserService {
	
	 public User getUserById(String userId);
	 
	 public int updateUser(User user);
	 
}
