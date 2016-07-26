package com.wnt.service;

import java.util.List;

import com.wnt.domain.Permission;

public interface IPermissionService {

	public List<Permission> getUserActionsByUserid(String userid);
	
	public List<Permission> getUserActionsByUseridAndMenuid(String userid,int menuid);
	
	
}
