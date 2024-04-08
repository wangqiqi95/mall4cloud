package com.mall4j.cloud.biz.model;


import lombok.Data;

import java.util.Date;

/**
 * 任务提醒表
 */
@Data
public class TaskRemindInfo {
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
     * 提醒类型 1任务导购 2指定员工 3任务导购和指定员工
     */
    private Integer remindType;
    /**
     * 提醒场景 1新任务生成时 2当任务开始后x个小时 3当任务还剩x且任务完成率未达x
     */
    private Integer remindScenes;
    /**
     * 开始多少个小时后提醒
     */
    private Double startAfterHours;
    /**
     * 剩余多少个小时
     */
    private Double lastHours;
    /**
     * 达标比例
     */
    private Double standardScale;
}

