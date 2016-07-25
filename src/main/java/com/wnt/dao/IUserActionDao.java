package com.wnt.dao;

import java.util.List;

import com.wnt.domain.UserAction;

public interface IUserActionDao {
    
	List<UserAction> selectByUserid(String userid);
	
	List<UserAction> selectByUseridAndMenuid(String userid,int menuid);
}