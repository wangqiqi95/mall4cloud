package com.mall4j.cloud.api.platform.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工部门关联表
 *
 * @author gmq
 * @date 2023-10-27 10:44:14
 */
@Data
public class StaffOrgRelVO implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long relId;

    /**
     * 
     */
    private Long staffId;

    /**
     * 
     */
    private Long orgId;
}
