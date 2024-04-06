package com.mall4j.cloud.biz.model;


import lombok.Data;

import java.util.Date;

/**
 * 任务信息表
 */
@Data
public class TaskInfo {
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
     * 任务名称
     */
    private String taskName;
    /**
     * 任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户
     */
    private Integer taskType;
    /**
     * 分享方式 1企微单聊 2企微群发 3发朋友圈 4群发客户群。task_type=3时有效
     */
    private Integer shareType;
    /**
     * 任务客户类型 1全部客户 2指定标签 3导入客户
     */
    private Integer taskClientType;
    /**
     * 任务客户群类型 1全部客户群 2指定客户群
     */
    private Integer taskClientGroupType;
    /**
     * 分配数量
     */
    private Integer allocatedQuantity;
    /**
     * 执行方式 1导购执行 2系统执行
     */
    private Integer implementationType;
    /**
     * 话术
     */
    private String speechSkills;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 任务门店类型 1全部门店 2指定门店
     */
    private Integer taskStoreType;
    /**
     * 任务导购类型 1全部导购 2指定导购
     */
    private Integer taskShoppingGuideType;
    /**
     * 任务目标。部分任务不存在比例，该字段存储
     */
    private String taskTarget;
    /**
     * 任务目标比例
     */
    private Double taskTargetScale;
    /**
     * 任务频率 1单次任务 2周期任务
     */
    private Integer taskFrequency;
    /**
     * 回访结果提交 1是 0否
     */
    private Integer visitResultsType;
    /**
     * 任务状态 0未开始 1进行中 2已结束
     */
    private Integer taskStatus;
}

