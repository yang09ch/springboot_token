package com.kgc.service;

import com.kgc.pojo.User;

public interface UserService {
    //登录
    User login(String name,String pwd);
}
