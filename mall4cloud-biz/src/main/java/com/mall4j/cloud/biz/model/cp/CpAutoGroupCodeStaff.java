package com.mall4j.cloud.biz.model.cp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *
 * @author gmq
 * @date 2023-10-27 16:59:44
 */
@Data
public class CpAutoGroupCodeStaff extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活码id
     */
    private Long codeId;

    /**
     * 员工id
     */
    private Long staffId;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 企业微信用户id
     */
    private String userId;

    /**
     * 是否删除：0否/1是
     */
    private Integer isDelete;

    private String createBy;

    private String updateBy;

}
