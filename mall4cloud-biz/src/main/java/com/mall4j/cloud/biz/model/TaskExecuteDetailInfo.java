package com.mall4j.cloud.biz.model;


import lombok.Data;

import java.util.Date;

/**
 * 任务调度详情信息
 */
@Data
public class TaskExecuteDetailInfo {
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
     * 调度表id
     */
    private Long executeId;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 客户id
     */
    private String clientId;
    /**
     * 任务类型为加企微好友时特有字段。0未添加 1已添加 2添加失败
     */
    private Integer addStatus;
    /**
     * 客户群id
     */
    private String clientGroupId;
    /**
     * 状态 1完成 0未完成
     */
    private Integer status;
    /**
     * 结束时间
     */
    private Date endTime;
}

