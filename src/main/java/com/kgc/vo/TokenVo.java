package com.kgc.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVo {
    private String token;//token字符串，唯一的标识，返回客户端
    private long genTime;//生成时间
    private long expTime;//有效期时间
}
