package com.github.drizzleme.service;

import com.github.drizzleme.bo.User;

import java.util.List;

/**
 * Created with iBoot
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/10/8
 */
public interface UserService {
    List<User> selectAll();
}
