package com.mall4j.cloud.biz.model;


import lombok.Data;

import java.util.Date;

/**
 * 任务导购表
 */
@Data
public class TaskShoppingGuideInfo {
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
     * 导购id
     */
    private String shopGuideId;
    /**
     * 导购类型 1任务导购 2指定的员工导购
     */
    private Integer shopGuideType;
}

