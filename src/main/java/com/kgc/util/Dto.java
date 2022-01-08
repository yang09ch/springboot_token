package com.kgc.util;

import lombok.Data;

@Data
public class Dto {
    private String success;
    private String msg;//
    private Object data;//数据
    private String errorCode;//错误码

}
