package com.wnt.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.wnt.domain.User;
import com.wnt.domain.UserExtend;
import com.wnt.dao.IUserDao;
import com.wnt.dao.IUserExtendDao;
import com.wnt.service.IUserService;


@Service("userService")
public class UserServiceImpl implements IUserService {
	
	@Resource 
	private IUserDao userDao;
	
	@Resource 
	private IUserExtendDao userextendDao;
	
	@Override
	public User getUserById(String userid) {
		
		return this.userDao.selectByPrimaryKey(userid);
	}

	@Override
	public int updateUser(User user) {

		return this.userDao.updateByPrimaryKeySelective(user);
	}

	@Override
	public List<User> getAllUsers() {
		
		return this.userDao.selectALL();
	
	}

	@Override
	public List<UserExtend> getAllUserExtends() {

		return this.userextendDao.selectALLExtend();
	}

	@Override
	public int saveUserExtend(Map<String,Object> map){
		
		return this.userextendDao.insertUserExtend(map);
	}
}
