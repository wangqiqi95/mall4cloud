package com.mall4j.cloud.api.biz.dto.crm;

import lombok.Data;

import java.io.Serializable;

@Data
public class CpCrmWxUser implements Serializable {
    private static final long serialVersionUID = -1087908218936072418L;

    private String userId;

    private String name;

    private String mobile;

    private String position;

    private Integer gender;

    private String email;

    private String avatar;

    private String thumbAvatar;

    private String alias;

    private Integer status;

    private String address;

    private String departmentId;

    private String openUserId;

    private String change_type;

}
