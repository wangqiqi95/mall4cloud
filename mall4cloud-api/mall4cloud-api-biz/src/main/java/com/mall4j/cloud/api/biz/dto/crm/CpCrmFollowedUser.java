package com.mall4j.cloud.api.biz.dto.crm;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CpCrmFollowedUser implements Serializable {
    private static final long serialVersionUID = -1049905218936072418L;

    private String userId;

    private String remark;

    private String description;

    private Long created;

    private String state;

    private String remarkCompany;

    private List<String> remarkMobiles;

    private Integer addWay;

    private String operUserId;

}
