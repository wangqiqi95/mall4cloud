package com.mall4j.cloud.biz.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 任务客户临时表
 */
@Data
@TableName("cp_task_client_temp_info")
public class TaskClientTempInfo {
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
     * 临时id，用于新增时匹配
     */
    private String tempUuid;
    /**
     * 客户昵称
     */
    private String clientNickname;
    /**
     * 客户电话
     */
    private String clientPhone;
}

