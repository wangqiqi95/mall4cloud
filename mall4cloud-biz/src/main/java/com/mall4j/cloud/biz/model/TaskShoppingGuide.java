package com.mall4j.cloud.biz.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.util.Date;
import java.io.Serializable;

/**
 * 导购任务关联表(TaskShoppingGuide)实体类
 *
 * @author hlc
 * @since 2024-04-01 10:46:50
 */
public class TaskShoppingGuide extends BaseModel implements Serializable {
    private static final long serialVersionUID = -60631701559955155L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 任务表id
     */
    private Long taskId;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 导购id
     */
    private Long userId;
    /**
     * 状态 0未完成 1已完成
     */
    private Integer status;
    /**
     * 导购任务完成时间
     */
    private Date completionTime;
    
    private String updateBy;
    
    private String createBy;
    private Integer isDelete;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }


    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }


    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "TaskShoppingGuide{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", storeId=" + storeId +
                ", userId=" + userId +
                ", status=" + status +
                ", completionTime=" + completionTime +
                ", updateBy='" + updateBy + '\'' +
                ", createBy='" + createBy + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}

