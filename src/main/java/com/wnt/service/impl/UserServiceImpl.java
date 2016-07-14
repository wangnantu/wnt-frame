package com.wnt.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.dao.IUserDao;
import com.wnt.domain.User;
import com.wnt.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {
	
	@Resource 
	private IUserDao userDao; 
	
	@Override
	public User getUserById(String userid) {
		
		return this.userDao.selectByPrimaryKey(userid);
	}

	@Override
	public int updateUser(User user) {

		return this.userDao.updateByPrimaryKeySelective(user);
	}

}
