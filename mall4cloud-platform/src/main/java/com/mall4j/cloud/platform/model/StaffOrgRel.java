package com.mall4j.cloud.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工部门关联表
 *
 * @author gmq
 * @date 2023-10-27 10:44:14
 */
@Data
public class StaffOrgRel extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long staffId;

    /**
     * 
     */
    private Long orgId;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private Date createDate;

    /**
     * 
     */
    private String updateBy;

    /**
     * 是否删除：0否/1是
     */
    private Integer isDelete;

}
