package com.wnt.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.dao.IMenuDao;
import com.wnt.domain.Menu;
import com.wnt.service.IMenuService;

@Service("menuService")
public class MenuServiceImpl implements IMenuService {
	
	@Resource 
	private IMenuDao menuDao;

	@Override
	public Menu getMenuById(int menuid) {
		
		return this.menuDao.selectByPrimaryKey(menuid);
	}

	@Override
	public int getSoncnt(int menuid) {
		if(0 == menuid){
			return 0;
		}else{
			return this.menuDao.getSoncnt(menuid);
		}
	}
	
	@Override
	public List<Integer> getMenusByUserid(String userid) {

		return this.menuDao.selectMenusByUserid(userid);
	}

}
