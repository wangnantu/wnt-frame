package com.wnt.dao;

import java.util.List;

import com.wnt.domain.Permission;

public interface IPermissionDao {
    
	List<Permission> selectActionsByUserid(String userid);
	
	List<Permission> selectActionsByUseridAndMenuid(String userid,int menuid);
	
	
}