package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户签到实体")
public class UserSignDTO implements Serializable {
    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;
    @ApiModelProperty(value = "用户手机号",hidden = true)
    private String mobile;
    @ApiModelProperty(value = "门店id",hidden = true)
    private Long shopId;
    @ApiModelProperty("活动id")
    private Integer activityId;
    @ApiModelProperty(value = "门店名称",hidden = true)
    private String shopName;
    @ApiModelProperty(value = "门店编码",hidden = true)
    private String shopCode;
    @ApiModelProperty("连签奖励id")
    private Integer rewardId;
}
