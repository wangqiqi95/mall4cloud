package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DistributionStoreActivityUserQueryDTO {

    @NotNull(message = "门店活动Id不能为空")
    @ApiModelProperty("门店活动Id")
    private Long activityId;
    @ApiModelProperty("关键词")
    private String keyword;
    @ApiModelProperty("会员名称")
    private String userName;
    @ApiModelProperty("会员手机号")
    private String userMobile;
    @ApiModelProperty("报名状态 -1 全部 0-已报名 1-已取消")
    private Integer status;

}
