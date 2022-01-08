package com.kgc.service.impl;

import com.kgc.mapper.UserMapper;
import com.kgc.pojo.User;
import com.kgc.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    //数据访问层对象
    @Resource
    UserMapper userMapper;

    @Override
    public User login(String name, String pwd) {
        return userMapper.getUser(name,pwd);
    }
}
