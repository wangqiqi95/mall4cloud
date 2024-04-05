package com.mall4j.cloud.api.user.dto;

import lombok.Data;

@Data
public class CPUserDto {
//    客户id，客户姓名，客户外部联系人id，性别，等级，手机号码，服务关系
    private String userQiWeiUserId;

    private String userName;
    private Long userId;
    private String customerName;
    private String levelName;
    private Integer level;
    private String mobile;
    private String relationStatus;

}
