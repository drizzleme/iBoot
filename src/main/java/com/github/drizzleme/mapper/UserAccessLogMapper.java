package com.github.drizzleme.mapper;

import com.github.drizzleme.bo.UserAccessLog;

public interface UserAccessLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAccessLog record);

    int insertSelective(UserAccessLog record);

    UserAccessLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAccessLog record);

    int updateByPrimaryKey(UserAccessLog record);
}