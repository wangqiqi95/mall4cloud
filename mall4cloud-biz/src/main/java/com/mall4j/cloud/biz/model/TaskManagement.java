package com.mall4j.cloud.biz.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;


/**
 * 任务管理记录表(TaskManagement)实体类
 *
 * @author hlc
 * @since 2024-04-01 10:46:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("task_management")
public class TaskManagement extends BaseModel implements Serializable {
    private static final long serialVersionUID = 149000625648654185L;
    /**
     * 任务id
     */
    private Long id;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 门店任务类型 0全部 1指定
     */
    private Integer storeSelectionType;
    /**
     * 导购任务范围类型 0全部 1指定
     */
    private Integer userRangeType;
    /**
     * 任务类型  0加企微好友 1 分享素材 2回访客户
     */
    private Integer taskType;
    /**
     * 所属公司类型 0麦吉利
     */
    private Integer affiliatedCompanyType;
    /**
     * 任务每天限制分配数量
     */
    private Integer quantityAllotted;
    /**
     * 执行方式 0导购执行 1 系统执行(需要对接机器人) 2一键转发
     */
    private Integer executionMode;
    /**
     * 话术内容
     */
    private String scriptContent;
    /**
     * 任务目标比例
     */
    private BigDecimal taskObjectiveRatio;
    /**
     * 任务开始时间
     */
    private Date taskStartTime;
    /**
     * 任务结束时间
     */
    private Date taskEndTime;
    /**
     * 回访结果 0无需提交 1需要提交
     */
    private Integer returnVisitResult;
    /**
     * 任务状态 0：未开始、1：进行中、2：已结束
     */
    private Integer status;
    private Integer isDelete;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getStoreSelectionType() {
        return storeSelectionType;
    }

    public void setStoreSelectionType(Integer storeSelectionType) {
        this.storeSelectionType = storeSelectionType;
    }

    public Integer getUserRangeType() {
        return userRangeType;
    }

    public void setUserRangeType(Integer userRangeType) {
        this.userRangeType = userRangeType;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getAffiliatedCompanyType() {
        return affiliatedCompanyType;
    }

    public void setAffiliatedCompanyType(Integer affiliatedCompanyType) {
        this.affiliatedCompanyType = affiliatedCompanyType;
    }

    public Integer getQuantityAllotted() {
        return quantityAllotted;
    }

    public void setQuantityAllotted(Integer quantityAllotted) {
        this.quantityAllotted = quantityAllotted;
    }

    public Integer getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(Integer executionMode) {
        this.executionMode = executionMode;
    }

    public String getScriptContent() {
        return scriptContent;
    }

    public void setScriptContent(String scriptContent) {
        this.scriptContent = scriptContent;
    }

    public BigDecimal getTaskObjectiveRatio() {
        return taskObjectiveRatio;
    }

    public void setTaskObjectiveRatio(@NotNull(message = "导购任务类型为必传项") BigDecimal taskObjectiveRatio) {
        this.taskObjectiveRatio = taskObjectiveRatio;
    }

    public Date getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Date taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public Date getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Date taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public Integer getReturnVisitResult() {
        return returnVisitResult;
    }

    public void setReturnVisitResult(Integer returnVisitResult) {
        this.returnVisitResult = returnVisitResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }


    @Override
    public String toString() {
        return "TaskManagement{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", storeSelectionType=" + storeSelectionType +
                ", userRangeType=" + userRangeType +
                ", taskType=" + taskType +
                ", affiliatedCompanyType=" + affiliatedCompanyType +
                ", quantityAllotted=" + quantityAllotted +
                ", executionMode=" + executionMode +
                ", scriptContent='" + scriptContent + '\'' +
                ", taskObjectiveRatio=" + taskObjectiveRatio +
                ", taskStartTime=" + taskStartTime +
                ", taskEndTime=" + taskEndTime +
                ", returnVisitResult=" + returnVisitResult +
                ", status=" + status +
                ", isDelete=" + isDelete +
                '}';
    }
}

