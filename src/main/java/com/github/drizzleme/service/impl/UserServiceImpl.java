package com.github.drizzleme.service.impl;

import com.github.drizzleme.bo.User;
import com.github.drizzleme.bo.UserAccessLog;
import com.github.drizzleme.bo.UserInfo;
import com.github.drizzleme.bo.UserRolePermission;
import com.github.drizzleme.mapper.*;
import com.github.drizzleme.service.UserService;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with iBoot
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/10/8
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    UserAccessLogMapper userAccessLogMapper;

    @Autowired
    UserRolePermissionMapper userRolePermissionMapper;

    @Override
    public List<User> selectAll(){
        return userMapper.selectAll();
    }

    @Override
    public UserInfo getUserInfoByUserName(String username) {
        return userInfoMapper.selectByUsername(username);
    }

    @Override
    public UserInfo getUserInfoByUserPhone(String userPhone) {
        return userInfoMapper.selectByPhone(userPhone);
    }

    @Override
    public UserRolePermission getRolePerByUidWithRid(Integer uid, Integer roleId) {
        return userRolePermissionMapper.selectByUidAndRoleId(uid, roleId);
    }

    @Override
    public void insertUserAccessLog(UserAccessLog userAccessLog) {
        userAccessLogMapper.insertSelective(userAccessLog);
    }
}
