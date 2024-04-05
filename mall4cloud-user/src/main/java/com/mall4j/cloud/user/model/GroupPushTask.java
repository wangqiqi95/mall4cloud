package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 群发任务组表
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_push_task")
@ApiModel(value="GroupPushTask对象", description="群发任务组表")
public class GroupPushTask extends Model<GroupPushTask> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "group_push_task_id", type = IdType.AUTO)
    private Long groupPushTaskId;

    @ApiModelProperty(value = "任务模式：0-客户群发 1-群群发")
    private Integer taskMode;

    @ApiModelProperty(value = "任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "触达人群：0-全部客户触达 1-按客户标签筛选 2-按客户分组筛选")
    private Integer tagType;

    @ApiModelProperty(value = "操作状态，0创建中，1可用，2创建失败，3编辑中, 4编辑失败")
    private Integer operateStatus;

    @ApiModelProperty(value = "失败时的参数")
    private String failParam;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "更新用户ID")
    private Long updateUserId;

    @ApiModelProperty(value = "更新人")
    private String updater;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新用户ID")
    private Date updateTime;

    @ApiModelProperty(value = "删除标识，0未删除，1删除")
    private Integer deleteFlag;

    @ApiModelProperty(value = "0待执行/1执行中/2已执行")
    private Integer executeStatus ;

    @ApiModelProperty("是否允许成员在待发送客户列表中重新进行选择：0否/1是")
    private Integer allowSelect;

    @Override
    protected Serializable pkVal() {
        return this.groupPushTaskId;
    }

}
