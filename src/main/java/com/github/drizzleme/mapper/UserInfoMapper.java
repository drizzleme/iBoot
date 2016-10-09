package com.github.drizzleme.mapper;

import com.github.drizzleme.bo.UserInfo;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);

    UserInfo selectByUsername(String username);

    UserInfo selectByPhone(String phone);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}