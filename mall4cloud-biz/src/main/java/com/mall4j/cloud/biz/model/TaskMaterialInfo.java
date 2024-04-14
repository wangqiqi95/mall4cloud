package com.mall4j.cloud.biz.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 任务素材信息表
 */
@Data
@TableName("cp_task_material_info")
public class TaskMaterialInfo {
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
     * 素材类型 图片（image）、语音(voice）、视频(video），普通文件(file〕"
     */
    private String materialType;
    /**
     * 素材保存信息
     */
    private String fileInfo;
}

