package com.jingshuiqi.dto;

import lombok.Data;

/**
 * @Author Administrator
 * @create 2019/9/20 0020 14:34
 */
@Data
public class UserAgent {
    private Integer id;
    private String openId;
    private Integer userType;
    private String parentOpenId;
    private Integer agentType;
}
