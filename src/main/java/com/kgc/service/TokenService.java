package com.kgc.service;

import com.kgc.pojo.User;

public interface TokenService {
    /**生成
     *
     * @param userAgent
     * @param user
     * @return
     */
     String genToken(String userAgent, User user);

    //保存到redis 存入redis
    void saveRedis(String token,User user);

    //从redis获取对象
    User getUserByRedis(String token);

    //从redis中删除token
     boolean delete(String token);

    String reload(String token,String userAgent)throws Exception;

}
