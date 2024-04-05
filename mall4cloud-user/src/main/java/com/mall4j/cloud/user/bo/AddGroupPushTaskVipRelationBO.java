package com.mall4j.cloud.user.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class AddGroupPushTaskVipRelationBO {

    @ApiModelProperty(value = "群发任务ID")
    private Long groupPushTaskId;

    @ApiModelProperty(value = "用户ID")
    private Long vipUserId;

    @ApiModelProperty(value = "会员卡号")
    private String vipCode;

    @ApiModelProperty(value = "导购ID")
    private Long staffId;

    @ApiModelProperty(value = "导购门店ID")
    private Long staffStoreId;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "导购企微ID")
    private String staffCpUserId;

    @ApiModelProperty(value = "客户企微ID")
    private String vipCpUserId;

    @ApiModelProperty(value = "是否加好友，0未加，1已加")
    private Integer friendState;



}
