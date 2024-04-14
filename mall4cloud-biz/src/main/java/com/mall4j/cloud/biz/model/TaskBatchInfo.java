package com.mall4j.cloud.biz.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 任务批次信息
 */
@Data
@TableName("cp_task_batch_info")
public class TaskBatchInfo {
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
     * 批次号
     */
    private Integer batchNum;
    /**
     * 任务开始时间
     */
    private Date startTime;
    /**
     * 任务结束时间
     */
    private Date endTime;
    /**
     * 当前批次任务状态 0进行中 1已结束
     */
    private Integer status;
    /**
     * -1代表全部门店，否则为指定门店
     */
    private Integer taskStoreNum;
    /**
     * -1代表全部导购，否则为指定导购
     */
    private Integer taskShoppingGuideNum;
}

