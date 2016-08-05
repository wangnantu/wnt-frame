package com.wnt.dao;

import java.util.List;

import com.wnt.domain.Menu;

public interface IMenuDao {
    int deleteByPrimaryKey(Integer menuid);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer menuid);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
    
    List<Integer> selectMenusByUserid(String userid);
    
    int getSoncnt(String userid,Integer menuid);
}