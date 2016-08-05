package com.wnt.service;

import java.util.List;

import com.wnt.domain.Menu;

public interface IMenuService {
	
	public Menu getMenuById(int menuid);
	
	public int getSoncnt(String userid,int menuid);

	List<Integer> getMenusByUserid(String userid);
	
}
