package com.mall4j.cloud.biz.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务回访信息信息
 * @TableName cp_task_visit_result_info
 */
@TableName(value ="cp_task_visit_result_info")
@Data
public class TaskVisitResultInfo implements Serializable {
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
     * 回访信息
     */
    private String resultInfo;

    /**
     * 附件信息
     */
    private String fileInfo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}