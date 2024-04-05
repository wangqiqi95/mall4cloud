package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupPushTaskVipRelationVO {

    @ApiModelProperty(value = "主键")
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

}
