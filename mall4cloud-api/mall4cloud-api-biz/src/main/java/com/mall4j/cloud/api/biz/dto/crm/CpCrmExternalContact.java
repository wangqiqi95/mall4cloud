package com.mall4j.cloud.api.biz.dto.crm;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CpCrmExternalContact implements Serializable {
    private static final long serialVersionUID = -1049085218936072418L;

    private String externalUserId;

    private String position;

    private String name;

    private String avatar;

    private String corpName;

    private String corpFullName;

    private Integer type;

    private Integer gender;

    private String unionId;

    private String change_type;

    private List<CpCrmFollowedUser> follow_user;
}
