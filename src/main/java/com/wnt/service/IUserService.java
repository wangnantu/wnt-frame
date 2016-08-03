package com.wnt.service;

import java.util.List;

import com.wnt.domain.User;
import com.wnt.domain.UserExtend;

public interface IUserService {
	
	 public User getUserById(String userid);
	 
	 public int updateUser(User user);
	 
	 public List<User> getAllUsers();
	 
	 public List<UserExtend> getAllUserExtends();
}
