package com.wnt.dao;

import java.util.List;

import com.wnt.domain.User;

public interface IUserDao {
    int deleteByPrimaryKey(String userid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    List<User> selectALL();
}