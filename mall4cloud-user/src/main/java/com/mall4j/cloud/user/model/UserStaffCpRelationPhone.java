package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author gmq
 * @date 2023-11-28 14:35:39
 */
@Data
public class UserStaffCpRelationPhone implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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
