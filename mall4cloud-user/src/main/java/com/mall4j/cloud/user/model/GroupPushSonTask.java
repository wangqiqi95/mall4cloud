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
 * 群发任务子任务表
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_push_son_task")
@ApiModel(value="GroupPushSonTask对象", description="群发任务子任务表")
public class GroupPushSonTask extends Model<GroupPushSonTask> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "group_push_son_task_id", type = IdType.AUTO)
    private Long groupPushSonTaskId;

    @ApiModelProperty(value = "推送名称（子任务名称）")
    private String sonTaskName;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "推送内容")
    private String pushContent;

    private Long groupPushTaskId;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "删除标识，0未删除，1已删除")
    private Integer deleteFlag;

    @ApiModelProperty(value = "1-正常/2-中止")
    private Integer status;

    private String updateBy;

    private Date updateTime;

    @ApiModelProperty(value = "任务类型：0-SCRM任务 1-CDP任务")
    private Integer taskType;

    @Override
    protected Serializable pkVal() {
        return this.groupPushSonTaskId;
    }

}
