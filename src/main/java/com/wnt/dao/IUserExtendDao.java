package com.wnt.dao;

import java.util.List;
import java.util.Map;

import com.wnt.domain.UserExtend;

public interface IUserExtendDao {
	
	List<UserExtend> selectALLExtend();
	
	int insertUserExtend(Map<String,Object> map);
}