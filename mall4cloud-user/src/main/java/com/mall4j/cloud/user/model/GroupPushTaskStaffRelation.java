package com.mall4j.cloud.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_push_task_staff_relation")
@ApiModel(value="GroupPushTaskStaffRelation对象", description="群发任务用户关联表")
public class GroupPushTaskStaffRelation extends Model<GroupPushTaskStaffRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "task_staff_relation_id", type = IdType.AUTO)
    private Long taskStaffRelationId;

    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "员工ID")
    private Long staffId;

    @ApiModelProperty(value = "员工name")
    private String staffName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "员工企业微信ID")
    private String staffCpUserId;


    @Override
    protected Serializable pkVal() {
        return this.taskStaffRelationId;
    }

}
