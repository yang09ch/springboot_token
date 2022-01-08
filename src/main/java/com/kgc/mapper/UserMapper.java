package com.kgc.mapper;

import com.kgc.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    //登录
     User getUser(@Param("name")String name,
                        @Param("pwd") String pwd);
}
