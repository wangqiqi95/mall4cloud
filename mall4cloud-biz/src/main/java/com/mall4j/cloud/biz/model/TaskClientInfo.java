package com.mall4j.cloud.biz.model;


import lombok.Data;

import java.util.Date;

/**
 * 任务客户表
 */
@Data
public class TaskClientInfo {
    /**
     * 主键
     */
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
}

