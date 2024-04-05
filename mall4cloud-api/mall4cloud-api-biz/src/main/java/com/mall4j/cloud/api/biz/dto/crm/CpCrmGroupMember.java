package com.mall4j.cloud.api.biz.dto.crm;

import lombok.Data;

import java.io.Serializable;

@Data
public class CpCrmGroupMember implements Serializable {
    private static final long serialVersionUID = -104098218936072418L;

    private String userId;
    private Integer type;
    private String unionId;
    private String joinScene;
    private String invitorUserid;
    private String nickName;
    private String name;
    private Long joinTime;
}
