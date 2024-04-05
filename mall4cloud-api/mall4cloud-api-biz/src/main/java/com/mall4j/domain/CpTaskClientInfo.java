package com.mall4j.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务客户表
 * @TableName cp_task_client_info
 */
@TableName(value ="cp_task_client_info")
@Data
public class CpTaskClientInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 逻辑删除
     */
    private Integer delFlag;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 客户id)
     */
    private String clientId;

    /**
     * 客户昵称
     */
    private String clientNickname;

    /**
     * 客户电话
     */
    private String clientPhone;

    /**
     * 客户备注
     */
    private String clientRemark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}