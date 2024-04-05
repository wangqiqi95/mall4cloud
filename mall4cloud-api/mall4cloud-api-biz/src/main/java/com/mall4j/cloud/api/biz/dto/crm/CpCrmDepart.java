package com.mall4j.cloud.api.biz.dto.crm;

import lombok.Data;

import java.io.Serializable;

@Data
public class CpCrmDepart implements Serializable {
    private static final long serialVersionUID = -1049908218936072418L;

    private Long departmentId;
    private Long parentId;
    private Long order;

    private String departmentName;
    private String departmentEnName;

    private String change_type;

}
