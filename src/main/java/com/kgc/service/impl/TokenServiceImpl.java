package com.kgc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kgc.pojo.User;
import com.kgc.service.TokenService;
import com.kgc.util.MD5;
import com.kgc.util.RedisUtil;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {
    //redis工具类对象
    @Resource
    RedisUtil redisUtil;
    //展示  token开头  此变量进行拼接字符串 提升作用域
    String tokenPrefix="token:";
    @Override
    public String genToken(String userAgent, User user) {
        StringBuilder sb=new StringBuilder(tokenPrefix);
        //获取用户代理对象
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        if(agent.getOperatingSystem().isMobileDevice()){
            sb.append("MOBILE-");//手机端
        }else{
            sb.append("PC-");//pc(电脑)端
        }
        // MD5 加密  拼接
        sb.append(MD5.getMd5(user.getName(),32)+"-");
        sb.append(user.getId()+"-");//用户id 拼接
        //时间 拼接
        sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"-");
        //再次MD5 加密 +拼接
        sb.append(MD5.getMd5(userAgent,6));
        return sb.toString();
    }
    @Override
    public void saveRedis(String token, User user) {
        //判断访问的客户端
        if(token.startsWith(tokenPrefix+"PC-")) {
            //当是pc端是进行有效期设置
            //有效期两个小时 +存进redis中
            redisUtil.set(token, JSON.toJSONString(user),2*60*60);
        }else{
            redisUtil.set(token,JSON.toJSONString(user));
        }
    }
    @Override
    public User getUserByRedis(String token) {
        //如果在redis中不存在 token return null
        if(!redisUtil.hasKey(token)){
            return  null;
        }
        //通过 key 从redis中取
        String jsonStr = redisUtil.get(token).toString();
        User user = JSON.parseObject(jsonStr,User.class);
        return user;
    }
    @Override
    public boolean delete(String token) {
             if (redisUtil.hasKey(token)){
                 redisUtil.del(token);
                 return  true;
             }
             return  false;
    }

    @Override
    public String reload(String token, String userAgent)throws Exception {
        if (!redisUtil.hasKey(token)){
            throw new Exception("令牌错误");
        }
        //判断时间
         String tokenTime=token.split("-")[3];//获取时间字符串
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");//转换时间格式
        Date date = sdf.parse(tokenTime);//变成时间格式
        //计算生效时间
        long diff=System.currentTimeMillis()-date.getTime();
        //声明一个变量设置 需要失效时间
        long reloadTime=60*60*1000;
        //失效时间
        long lastTime=2*60*60*1000-diff;//失效时间的毫秒值
        if (lastTime>reloadTime){
            throw new Exception("剩余时间超过60分钟，不能置换");
        }
        //置换
        //根据旧token获取对象
        User user = JSON.parseObject(redisUtil.get(token).toString(), User.class);
        //调用业务创建新token
        String newToken = this.genToken(userAgent, user);
        //调用业务保存金redis中
        this.saveRedis(newToken,user);
        this.delete(token);
        //返回新token
        return newToken;
    }
}
