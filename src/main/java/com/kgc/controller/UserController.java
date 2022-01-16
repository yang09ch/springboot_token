package com.kgc.controller;

import com.kgc.pojo.User;
import com.kgc.service.TokenService;
import com.kgc.service.UserService;
import com.kgc.util.Dto;
import com.kgc.util.DtoUtil;
import com.kgc.vo.TokenVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    //业务对象
    @Resource
    UserService userService;

    //token对象
    @Resource
    TokenService tokenService;

    @RequestMapping("/login")
    public Dto login(String name, String pwd, HttpServletRequest request){
        User user = userService.login(name, pwd);
        if(user!=null){
            //获取用户代理字符串
            String userAgent = request.getHeader("User-Agent");
            System.out.println("userAgent:"+userAgent);
            //调用业务   生成token
            String token = tokenService.genToken(userAgent, user);
            //保存入reids中
            tokenService.saveRedis(token,user);
            //封装一个tokenVo对象
            TokenVo tokenVo=new TokenVo();
            tokenVo.setToken(token);//
            tokenVo.setGenTime(System.currentTimeMillis());//毫秒   开始时间
            tokenVo.setExpTime(System.currentTimeMillis()+2*60*60*1000);//毫秒  结束时间
            //成功
            return DtoUtil.returnSuccess("登录成功",tokenVo);
        }else{
            //失败
            return  DtoUtil.returnFail("登录失败","10001");
        }

    }

    @RequestMapping("/getUser")
    public Dto getUser(HttpServletRequest request){
        String token = request.getHeader("token");
        User userByRedis = tokenService.getUserByRedis(token);
        if (userByRedis!=null){
            return DtoUtil.returnSuccess("获取成功",userByRedis);
        }else {
            return DtoUtil.returnFail("获取失败","1002");
        }
    }


    @RequestMapping("/loinOut")//此方法用于注销
    public Dto loinOut(HttpServletRequest request){
        String token = request.getHeader("token");
        if (tokenService.delete(token)){
            return DtoUtil.returnSuccess("成功");
        }else {
            return DtoUtil.returnFail("注销失败","1003");
        }
    }

    @RequestMapping("/reload")
    public Dto reload(HttpServletRequest request){
        //拿到odd token
        String token = request.getHeader("token");
        //获取用户代理字符串
        String userAgent = request.getHeader("User-Agent");
        try {
            String reload = tokenService.reload(token, userAgent);
            TokenVo tokenVo=new TokenVo();
            tokenVo.setToken(reload);//
            tokenVo.setGenTime(System.currentTimeMillis());//毫秒   开始时间
            tokenVo.setExpTime(System.currentTimeMillis()+2*60*60*1000);//毫秒  结束时间
            return DtoUtil.returnSuccess("置换成功",tokenVo);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("置换失败","1003");
        }
    }
}
