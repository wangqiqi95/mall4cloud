package com.mall4j.cloud.biz.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 任务调度信息
 */
@Data
@TableName("cp_task_execute_info")
public class TaskExecuteInfo {
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
     * 任务类型 1加企微好友 2好友转会员 3分享素材 4回访客户
     */
    private Integer taskType;
    /**
     * 导购id
     */
    private String shoppingGuideId;
    /**
     * 状态 1完成 0未完成
     */
    private Integer status;
    /**
     * 任务时间
     */
    private Date taskTime;
    /**
     * 需完成数量
     */
    private Integer clientSum;
    /**
     * 已完成数量
     */
    private Integer successClientSum;
    /**
     * 批次id(一个任务对应多个批次，一个批次对应多个导购任务调度，一个导购任务调度对应对个客户)
     */
    private Long batchId;
}

