package com.wnt.service;

import java.util.List;

import com.wnt.domain.UserAction;

public interface IUserActionService {

	public List<UserAction> getUserActionsByUserid(String userid);
	
	public List<UserAction> getUserActionsByUseridAndMenuid(String userid,int menuid);
	
}
