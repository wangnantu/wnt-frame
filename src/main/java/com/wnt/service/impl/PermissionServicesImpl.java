package com.wnt.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.dao.IPermissionDao;
import com.wnt.domain.Permission;
import com.wnt.service.IPermissionService;

@Service("permissionService")
public class PermissionServicesImpl implements IPermissionService {

	@Resource
	private IPermissionDao permissionDao;
	
	@Override
	public List<Permission> getUserActionsByUserid(String userid) {
		
		return this.permissionDao.selectActionsByUserid(userid);
	}

	@Override
	public List<Permission> getUserActionsByUseridAndMenuid(String userid,int menuid) {

		return this.permissionDao.selectActionsByUseridAndMenuid(userid, menuid);
	}

	
}
