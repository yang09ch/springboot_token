package com.kgc.util;
//该工具类封装Dto  函数重载
public class DtoUtil {
    public  static String success="success";//进行初始化
    public  static String fail="fail";//进行初始化
    /***
     * 统一返回成功的DTO
     */
    public static Dto returnSuccess(){
        Dto dto=new Dto();
        dto.setSuccess(success);
        return  dto;
    }
    public  static Dto  returnSuccess(String msg){
        Dto dto=new Dto();
        dto.setSuccess(success);
        dto.setMsg(msg);
        return dto;
    }
    /***
     * 统一返回成功的DTO 带数据
     */
    public  static Dto  returnSuccess(String msg,Object data){
        Dto dto=new Dto();
        dto.setSuccess(success);
        dto.setMsg(msg);
        dto.setData(data);
        return dto;
    }
   public  static Dto  returnSuccess(String success,String msg,Object data){
        Dto dto=new Dto();
        dto.setSuccess(success);
        dto.setMsg(msg);
        dto.setData(data);
        return dto;
    }

    public  static Dto  returnFail(String msg,Object data,String errCode){
        Dto dto=new Dto();
        dto.setSuccess(fail);
        dto.setMsg(msg);
        dto.setData(data);
        dto.setErrorCode(errCode);
        return dto;
    }
    public  static Dto  returnFail(String msg,String errCode){
        Dto dto=new Dto();
        dto.setSuccess(fail);
        dto.setMsg(msg);
        dto.setErrorCode(errCode);
        return dto;
    }
}
