package com.github.drizzleme.service.impl;

import com.github.drizzleme.bo.User;
import com.github.drizzleme.mapper.UserMapper;
import com.github.drizzleme.service.UserService;
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

    @Override
    public List<User> selectAll(){
        return userMapper.selectAll();
    }
}
