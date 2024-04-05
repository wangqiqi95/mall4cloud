package com.mall4j.cloud.api.platform.vo;

import lombok.Data;

@Data
public class StaffOrgVO {

    private Long staffId;

    private Long orgId;

    private String orgName;

    /**
     * 路径
     */
    private String path;

}
