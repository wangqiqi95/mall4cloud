package com.mall4j.cloud.api.user.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author gmq
 * @date 2023-11-28 14:35:39
 */
@Data
public class UserStaffCpRelationPhoneVO implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 
     */
    private Long relationId;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private Integer delFlag;

    private Integer status;

    private Long staffId;

    private String staffUserId;

}
