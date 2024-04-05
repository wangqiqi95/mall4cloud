package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChooseMemberEventMobileRelationVO {

    @ApiModelProperty(value = "主键（会员活动关联表ID）")
    private Long eventUserRelationId;

    @ApiModelProperty(value = "制定会员活动ID")
    private Long chooseMemberEventId;

    @ApiModelProperty(value = "用户绑定手机号")
    private String mobile;
}
