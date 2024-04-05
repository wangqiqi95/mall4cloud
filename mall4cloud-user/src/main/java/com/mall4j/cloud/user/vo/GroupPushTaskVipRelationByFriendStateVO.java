package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupPushTaskVipRelationByFriendStateVO {

    @ApiModelProperty("群发任务ID")
    private Long pushTaskId;

    @ApiModelProperty("导购ID")
    private Long staffId;

    @ApiModelProperty("会员ID")
    private Long userId;

    @ApiModelProperty("导购企业微信ID")
    private String staffCpUserId;

    @ApiModelProperty("客户企业微信ID")
    private String vipCpUserId;

    @ApiModelProperty("企业微信用户关系表中会员ID")
    private Long cpUserId;

    @ApiModelProperty("企业微信用户关系表中客户企业微信ID")
    private String qiWeiUserId;

    @ApiModelProperty("企业微信用户关系表中导购企业微信ID")
    private String qiWeiStaffId;

}
