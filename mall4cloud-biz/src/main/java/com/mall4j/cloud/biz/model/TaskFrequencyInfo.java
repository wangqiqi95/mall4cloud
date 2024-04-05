package com.mall4j.cloud.biz.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务频率信息
 * @TableName cp_task_frequency_info
 */
@TableName(value ="cp_task_frequency_info")
@Data
public class TaskFrequencyInfo implements Serializable {
    /**
     * 主键
     */
    @TableId
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
     * 开始时间。单次任务
     */
    private Date startTime;

    /**
     * 结束时间。单次任务
     */
    private Date endTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}