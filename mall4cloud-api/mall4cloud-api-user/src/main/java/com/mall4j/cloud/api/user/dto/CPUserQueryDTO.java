package com.mall4j.cloud.api.user.dto;

import lombok.Data;

@Data
public class CPUserQueryDTO {
    private String staffQiWeiUserId;
    private String staffId;
//    客户姓名
    private String userName;
    private Integer level;
    private String  levelName;
    //服务关系
    private Integer status;
}
