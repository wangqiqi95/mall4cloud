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

/**
 * <p>
 * 推送任务用户关联表
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_push_task_vip_relation")
@ApiModel(value="GroupPushTaskVipRelation对象", description="推送任务用户关联表")
public class GroupPushTaskVipRelation extends Model<GroupPushTaskVipRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "group_push_task_vip_relation_id", type = IdType.AUTO)
    private Long groupPushTaskVipRelationId;

    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "用户ID")
    private Long vipUserId;

    @ApiModelProperty(value = "会员卡号")
    private String vipCode;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "服务门店ID")
    private Long staffStoreId;

    @ApiModelProperty(value = "创建人ID")
    private String createUserId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "导购企业微信ID")
    private String staffCpUserId;

    @ApiModelProperty(value = "是否加好友，0未加，1已加")
    private Integer friendState;


    @ApiModelProperty(value = "客户企业微信ID")
    private String vipCpUserId;


    @Override
    protected Serializable pkVal() {
        return this.groupPushTaskVipRelationId;
    }

}
