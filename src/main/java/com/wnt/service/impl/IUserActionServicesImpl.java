package com.wnt.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.dao.IUserActionDao;
import com.wnt.domain.UserAction;
import com.wnt.service.IUserActionService;

@Service("userActionService")
public class IUserActionServicesImpl implements IUserActionService {

	@Resource
	private IUserActionDao userActionDao;
	
	@Override
	public List<UserAction> getUserActionsByUserid(String userid) {
		
		return this.userActionDao.selectByUserid(userid);
	}

	@Override
	public List<UserAction> getUserActionsByUseridAndMenuid(String userid,int menuid) {

		return this.userActionDao.selectByUseridAndMenuid(userid, menuid);
	}

}
