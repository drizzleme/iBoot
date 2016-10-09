package com.github.drizzleme.mapper;

import com.github.drizzleme.bo.UserRolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserRolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRolePermission record);

    int insertSelective(UserRolePermission record);

    UserRolePermission selectByPrimaryKey(Integer id);

    @Select("select * from user_role_permission where user_id = #{uid} and role_id = #{roleId}")
    UserRolePermission selectByUidAndRoleId(@Param("uid") Integer uid, @Param("roleId") Integer roleId);

    int updateByPrimaryKeySelective(UserRolePermission record);

    int updateByPrimaryKey(UserRolePermission record);
}