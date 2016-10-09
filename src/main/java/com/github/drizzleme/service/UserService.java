package com.github.drizzleme.service;

import com.github.drizzleme.bo.User;
import com.github.drizzleme.bo.UserAccessLog;
import com.github.drizzleme.bo.UserInfo;
import com.github.drizzleme.bo.UserRolePermission;

import java.util.List;

/**
 * Created with iBoot
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/10/8
 */
public interface UserService {
    List<User> selectAll();

    UserInfo getUserInfoByUserName(String username);

    UserInfo getUserInfoByUserPhone(String userPhone);

    UserRolePermission getRolePerByUidWithRid(Integer uid, Integer roleId);

    void insertUserAccessLog(UserAccessLog userAccessLog);
}
