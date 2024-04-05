package com.mall4j.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务门店表
 * @TableName cp_task_store_info
 */
@TableName(value ="cp_task_store_info")
@Data
public class CpTaskStoreInfo implements Serializable {
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
     * 门店id
     */
    private String storeId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}